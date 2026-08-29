package mohamedjaouad.TRAINOVA.controllers;

import mohamedjaouad.TRAINOVA.entities.WorkoutSession;
import mohamedjaouad.TRAINOVA.entities.SessionExercise;
import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.entities.Exercise;
import mohamedjaouad.TRAINOVA.exceptions.NotFoundException;
import mohamedjaouad.TRAINOVA.recordsDTO.SaveWorkoutRequest;
import mohamedjaouad.TRAINOVA.recordsDTO.SaveWorkoutExerciseRequest;
import mohamedjaouad.TRAINOVA.repositories.ExerciseRepository;
import mohamedjaouad.TRAINOVA.repositories.WorkoutRepository;
import mohamedjaouad.TRAINOVA.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public WorkoutController(
            WorkoutRepository workoutRepository,
            ExerciseRepository exerciseRepository,
            UserRepository userRepository
    ) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<WorkoutSession> getMyWorkouts(@AuthenticationPrincipal User currentUser) {
        return workoutRepository.findByUserIdOrderBySessionDateDesc(currentUser.getId());
    }

    @GetMapping("/{id}")
    public WorkoutSession getWorkoutById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return workoutRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new NotFoundException("Workout non trovato"));
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public WorkoutSession saveWorkout(
            @RequestBody @Valid SaveWorkoutRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        WorkoutSession session = new WorkoutSession();
        session.setUser(currentUser);
        session.setTitle(request.title());
        session.setType(request.type());
        session.setIntensity(request.intensity() == null || request.intensity().isBlank() ? "Completato" : request.intensity());
        session.setDuration(request.duration() == null ? 0 : request.duration());
        session.setCalories(0);
        session.setNotes(request.notes());

        int totalVolume = 0;
        int totalSets = 0;
        for (SaveWorkoutExerciseRequest item : request.exercises()) {
            Exercise exercise = exerciseRepository.findById(item.exerciseId())
                    .orElseThrow(() -> new NotFoundException("Esercizio non trovato"));

            SessionExercise sessionExercise = new SessionExercise();
            sessionExercise.setWorkoutSession(session);
            sessionExercise.setExercise(exercise);
            sessionExercise.setSets(item.sets());
            sessionExercise.setReps(item.reps());
            sessionExercise.setWeight(item.weightKg() == null ? "-" : item.weightKg() + " kg");
            sessionExercise.setRest(item.rest() == null ? "-" : item.rest());
            session.getExercises().add(sessionExercise);

            totalSets += item.sets();
            if (item.weightKg() != null) {
                totalVolume += (int) Math.round(item.weightKg() * item.sets() * estimatedReps(item.reps()));
            }
        }
        session.setVolume(totalVolume);

        WorkoutSession saved = workoutRepository.save(session);

        addExperience(currentUser, totalSets, totalVolume);

        return saved;
    }

    private int estimatedReps(String reps) {
        String firstNumber = reps.replaceAll("[^0-9].*", "");
        try {
            return Integer.parseInt(firstNumber);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private void addExperience(User user, int totalSets, int totalVolume) {
        int xpGain = 0;

        xpGain += totalSets * 5;

        if (totalVolume > 0) {
            xpGain += Math.min(totalVolume / 10, 20);
        }

        if (user.getTrainingStyle() != null) {
            xpGain += 2;
        }

        int newXp = user.getXp() + xpGain;
        user.setXp(newXp);

        int newLevel = 1 + (newXp / 100);
        user.setLevel(newLevel);

        userRepository.save(user);
    }
}