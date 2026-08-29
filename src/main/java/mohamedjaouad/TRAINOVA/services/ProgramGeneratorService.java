package mohamedjaouad.TRAINOVA.services;

import mohamedjaouad.TRAINOVA.entities.*;
import mohamedjaouad.TRAINOVA.recordsDTO.GenerateProgramRequest;
import mohamedjaouad.TRAINOVA.repositories.ExerciseRepository;
import mohamedjaouad.TRAINOVA.repositories.ProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgramGeneratorService {

    private final ExerciseRepository exerciseRepository;
    private final ProgramRepository programRepository;

    private static final Map<String, Set<String>> MUSCLE_GROUPS_BY_DAY_TITLE = Map.ofEntries(
            Map.entry("Push", Set.of("Petto", "Spalle", "Braccia")),
            Map.entry("Pull", Set.of("Schiena", "Braccia")),
            Map.entry("Legs", Set.of("Gambe")),
            Map.entry("Upper Body", Set.of("Petto", "Schiena", "Spalle", "Braccia")),
            Map.entry("Lower Body", Set.of("Gambe", "Core")),
            Map.entry("Full Body A", Set.of()),
            Map.entry("Full Body B", Set.of()),
            Map.entry("Full Body", Set.of())
    );

    public ProgramGeneratorService(ExerciseRepository exerciseRepository, ProgramRepository programRepository) {
        this.exerciseRepository = exerciseRepository;
        this.programRepository = programRepository;
    }

    @Transactional
    public Program generateProgram(GenerateProgramRequest request, User currentUser) {
        List<Exercise> compatibleExercises = exerciseRepository.findAll().stream()
                .filter(e -> matchesSelectedEquipment(e.getEquipment(), request.equipment()))
                .collect(Collectors.toList());

        if (compatibleExercises.isEmpty()) {
            throw new RuntimeException("Nessun esercizio trovato per l'attrezzatura selezionata");
        }

        deactivatePreviousPrograms(currentUser.getId());

        Program program = new Program();
        program.setUser(currentUser);
        program.setGoal(request.goal());
        program.setStyle(request.style());
        program.setDaysPerWeek(request.daysPerWeek());
        program.setWeeksDuration(6);
        program.setName(request.goal().toUpperCase() + " - " + request.style());
        program.setActive(true);

        StyleProfile profile = StyleProfile.forStyle(request.style());

        List<ProgramDay> days = generateDays(request.daysPerWeek(), compatibleExercises, program, profile, request.level());
        program.setDays(days);

        return programRepository.save(program);
    }

    public List<Program> getActivePrograms(UUID userId) {
        return programRepository.findByUserIdAndActiveTrue(userId);
    }

    private void deactivatePreviousPrograms(UUID userId) {
        List<Program> active = programRepository.findByUserIdAndActiveTrue(userId);
        for (Program p : active) {
            p.setActive(false);
        }
        programRepository.saveAll(active);
    }

    private boolean matchesSelectedEquipment(String exerciseEquipment, List<String> selectedEquipment) {
        if (exerciseEquipment == null) {
            return false;
        }
        return selectedEquipment.stream().anyMatch(selected ->
                selected.equalsIgnoreCase(exerciseEquipment)
                        || (selected.equalsIgnoreCase("Cavi/Macchine")
                        && (exerciseEquipment.equalsIgnoreCase("Cavi")
                        || exerciseEquipment.equalsIgnoreCase("Macchina"))));
    }

    private List<ProgramDay> generateDays(int daysPerWeek, List<Exercise> exercises, Program program, StyleProfile profile, String level) {
        List<ProgramDay> days = new ArrayList<>();
        String[] dayTitles = switch (daysPerWeek) {
            case 2 -> new String[]{"Full Body A", "Full Body B"};
            case 3 -> new String[]{"Upper Body", "Lower Body", "Full Body"};
            case 4 -> new String[]{"Upper Body", "Lower Body", "Push", "Pull"};
            case 5 -> new String[]{"Push", "Pull", "Legs", "Upper Body", "Lower Body"};
            case 6 -> new String[]{"Push", "Pull", "Legs", "Push", "Pull", "Legs"};
            default -> new String[]{"Day 1", "Day 2", "Day 3"};
        };

        int exerciseCount = exerciseCountForLevel(level, exercises.size());
        int restSeconds = restSecondsForLevel(level, profile.restSeconds());

        Map<UUID, Integer> usageCount = new HashMap<>();
        for (Exercise e : exercises) {
            usageCount.put(e.getId(), 0);
        }

        Random random = new Random();

        for (int i = 0; i < daysPerWeek; i++) {
            String title = i < dayTitles.length ? dayTitles[i] : "Day " + (i + 1);

            ProgramDay day = new ProgramDay();
            day.setProgram(program);
            day.setTitle(title);
            day.setDayIndex(i);

            List<Exercise> dayPool = poolForDay(title, exercises);
            List<Exercise> picked = pickExercisesForDay(dayPool, exercises, exerciseCount, usageCount, random);

            List<ProgramExercise> dayExercises = new ArrayList<>();
            int orderIndex = 0;
            for (Exercise ex : picked) {
                ProgramExercise pe = new ProgramExercise();
                pe.setProgramDay(day);
                pe.setExercise(ex);
                pe.setSets(profile.sets());
                pe.setReps(profile.reps());
                pe.setRestSeconds(restSeconds);
                pe.setOrderIndex(orderIndex++);
                dayExercises.add(pe);

                usageCount.merge(ex.getId(), 1, Integer::sum);
            }
            day.setExercises(dayExercises);
            days.add(day);
        }
        return days;
    }

    private List<Exercise> poolForDay(String dayTitle, List<Exercise> allCompatible) {
        Set<String> targetGroups = MUSCLE_GROUPS_BY_DAY_TITLE.get(dayTitle);
        if (targetGroups == null || targetGroups.isEmpty()) {
            return allCompatible;
        }

        List<Exercise> filtered = allCompatible.stream()
                .filter(e -> e.getMuscleGroup() != null && targetGroups.contains(e.getMuscleGroup()))
                .toList();

        return filtered.size() >= 3 ? filtered : allCompatible;
    }

    private List<Exercise> pickExercisesForDay(
            List<Exercise> dayPool,
            List<Exercise> allCompatible,
            int exerciseCount,
            Map<UUID, Integer> usageCount,
            Random random
    ) {
        List<Exercise> candidates = new ArrayList<>(dayPool);
        Collections.shuffle(candidates, random);
        candidates.sort(Comparator.comparingInt(e -> usageCount.getOrDefault(e.getId(), 0)));

        List<Exercise> picked = new ArrayList<>();
        Set<UUID> pickedIds = new HashSet<>();
        for (Exercise e : candidates) {
            if (picked.size() >= exerciseCount) break;
            picked.add(e);
            pickedIds.add(e.getId());
        }

        if (picked.size() < exerciseCount) {
            List<Exercise> filler = new ArrayList<>(allCompatible);
            Collections.shuffle(filler, random);
            filler.sort(Comparator.comparingInt(e -> usageCount.getOrDefault(e.getId(), 0)));
            for (Exercise e : filler) {
                if (picked.size() >= exerciseCount) break;
                if (pickedIds.contains(e.getId())) continue;
                picked.add(e);
                pickedIds.add(e.getId());
            }
        }

        return picked;
    }

    private int exerciseCountForLevel(String level, int poolSize) {
        int desired = switch (normalizeLevel(level)) {
            case "principiante" -> 4;
            case "avanzato" -> 6;
            default -> 5;
        };
        return Math.min(desired, poolSize);
    }

    private int restSecondsForLevel(String level, int baseRest) {
        return switch (normalizeLevel(level)) {
            case "principiante" -> baseRest + 15;
            case "avanzato" -> Math.max(15, baseRest - 15);
            default -> baseRest;
        };
    }

    private String normalizeLevel(String level) {
        if (level == null) return "intermedio";
        return switch (level.trim().toLowerCase()) {
            case "beginner", "principiante" -> "principiante";
            case "advanced", "avanzato" -> "avanzato";
            default -> "intermedio";
        };
    }

    private record StyleProfile(int sets, String reps, int restSeconds) {
        static StyleProfile forStyle(String style) {
            if (style == null) return new StyleProfile(3, "8-12", 75);

            return switch (style.trim().toLowerCase()) {
                case "forza", "strength" -> new StyleProfile(5, "3-5", 180);
                case "resistenza", "endurance" -> new StyleProfile(3, "15-20", 30);
                case "ipertrofia", "hypertrophy" -> new StyleProfile(4, "8-12", 75);
                default -> new StyleProfile(3, "8-12", 75);
            };
        }
    }
}