package mohamedjaouad.TRAINOVA.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import mohamedjaouad.TRAINOVA.entities.*;
import mohamedjaouad.TRAINOVA.recordsDTO.AIProgramResponseDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.GenerateProgramRequest;
import mohamedjaouad.TRAINOVA.repositories.ExerciseRepository;
import mohamedjaouad.TRAINOVA.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AIProgramGeneratorService {


    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";


    private static final String MODEL = "openai/gpt-oss-120b";

    private final ExerciseRepository exerciseRepository;
    private final ProgramRepository programRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String apiKey;

    public AIProgramGeneratorService(
            ExerciseRepository exerciseRepository,
            ProgramRepository programRepository,
            ObjectMapper objectMapper,
            @Value("${groq.api.key}") String apiKey
    ) {
        this.exerciseRepository = exerciseRepository;
        this.programRepository = programRepository;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    @Transactional
    public Program generateProgram(GenerateProgramRequest request, User currentUser) {
        List<Exercise> compatibleExercises = exerciseRepository.findAll().stream()
                .filter(e -> matchesSelectedEquipment(e.getEquipment(), request.equipment()))
                .collect(Collectors.toList());

        if (compatibleExercises.isEmpty()) {
            throw new RuntimeException("Nessun esercizio trovato per l'attrezzatura selezionata");
        }

        String prompt = buildPrompt(request, compatibleExercises);
        String rawResponse = callGroq(prompt);
        AIProgramResponseDTO parsed = parseResponse(rawResponse);

        Program program = mapToProgram(parsed, request, currentUser, compatibleExercises);

        deactivatePreviousPrograms(currentUser.getId());

        return programRepository.save(program);
    }

    private String buildPrompt(GenerateProgramRequest request, List<Exercise> compatibleExercises) {
        String exerciseList = compatibleExercises.stream()
                .map(e -> "- " + e.getName() + " (gruppo muscolare: " + e.getMuscleGroup() + ", attrezzatura: " + e.getEquipment() + ")")
                .collect(Collectors.joining("\n"));

        String splitGuide = splitGuideForDays(request.daysPerWeek());

        return """
                Sei un personal trainer esperto. Devi creare una scheda di allenamento
                personalizzata in base a questi dati utente:

                - Obiettivo: %s
                - Livello: %s
                - Giorni di allenamento a settimana: %d
                - Stile preferito: %s

                REGOLA VINCOLANTE: puoi usare SOLO ed ESCLUSIVAMENTE i seguenti esercizi
                (copia il nome esattamente come scritto, non inventarne altri e non modificarne il nome):
                %s

                Requisiti sulla scheda:
                - Deve avere esattamente %d giorni, con questo split e questi
                  titoli di giorno (usali esattamente, nell'ordine indicato):
                  %s
                - REGOLA VINCOLANTE SUL GRUPPO MUSCOLARE: ogni esercizio inserito in un
                  giorno DEVE avere il campo "gruppo muscolare" coerente con il titolo di
                  quel giorno (es. un giorno "Push" deve contenere solo esercizi con
                  gruppo muscolare Petto, Spalle o Braccia; un giorno "Pull" solo Schiena
                  o Braccia; un giorno "Legs" solo Gambe; "Upper Body" solo Petto,
                  Schiena, Spalle o Braccia; "Lower Body" solo Gambe o Core; "Full Body"
                  può mescolare tutti i gruppi). Non inserire mai un esercizio il cui
                  gruppo muscolare non è coerente con il tema del giorno.
                - REGOLA VINCOLANTE SULLA VARIETÀ TRA I GIORNI: se lo split ripete lo
                  stesso tema più volte nella settimana (es. Push compare due volte in
                  uno split a 6 giorni), i due giorni con lo stesso tema NON devono avere
                  la stessa identica lista di esercizi: scegli combinazioni diverse
                  all'interno degli esercizi disponibili per quel gruppo muscolare,
                  variando la selezione il più possibile. Evita inoltre di ripetere lo
                  stesso identico ordine/lista di esercizi tra giorni diversi in generale.
                - Ogni giorno deve avere 4-6 esercizi, SENZA ripetizioni all'interno dello
                  stesso giorno.
                - Serie, ripetizioni e recupero devono riflettere lo stile scelto:
                  Forza = poche reps (3-5), carichi alti, recupero lungo (150-180s);
                  Ipertrofia = reps medie (8-12), recupero medio (60-90s);
                  Resistenza = reps alte (15-20), recupero breve (20-40s).
                - Adatta la selezione degli esercizi anche al livello dichiarato (un principiante
                  non dovrebbe partire con esercizi troppo tecnici se ci sono alternative più semplici
                  nella lista disponibile).

                Rispondi ESCLUSIVAMENTE con un oggetto JSON valido, senza testo prima o dopo,
                senza markdown, senza backtick, con questa forma esatta:

                {
                  "programName": "string",
                  "days": [
                    {
                      "title": "string",
                      "exercises": [
                        { "exerciseName": "string", "sets": number, "reps": "string es. 8-12", "restSeconds": number }
                      ]
                    }
                  ]
                }
                """.formatted(
                request.goal(),
                request.level(),
                request.daysPerWeek(),
                request.style(),
                exerciseList,
                request.daysPerWeek(),
                splitGuide
        );
    }


    private String splitGuideForDays(int daysPerWeek) {
        String[] titles = switch (daysPerWeek) {
            case 2 -> new String[]{"Full Body A", "Full Body B"};
            case 3 -> new String[]{"Upper Body", "Lower Body", "Full Body"};
            case 4 -> new String[]{"Upper Body", "Lower Body", "Push", "Pull"};
            case 5 -> new String[]{"Push", "Pull", "Legs", "Upper Body", "Lower Body"};
            case 6 -> new String[]{"Push", "Pull", "Legs", "Push", "Pull", "Legs"};
            default -> null;
        };
        if (titles == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= daysPerWeek; i++) {
                sb.append("Giorno ").append(i).append(": \"Day ").append(i).append("\" (Full Body)\n");
            }
            return sb.toString().trim();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < titles.length; i++) {
            sb.append("Giorno ").append(i + 1).append(": \"").append(titles[i]).append("\"\n");
        }
        return sb.toString().trim();
    }

    private String callGroq(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GROQ_API_KEY non configurata");
        }

        try {
            // Groq usa lo stesso formato di OpenAI (Chat Completion)
            String body = objectMapper.writeValueAsString(Map.of(
                    "model", MODEL,
                    "messages", List.of(Map.of("role", "user", "content", prompt)),
                    "temperature", 0.7,
                    "max_tokens", 4000
            ));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(GROQ_API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey) // Header corretto per Groq
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Chiamata a Groq fallita (status " + response.statusCode() + "): " + response.body());
            }

            return extractTextFromResponse(response.body());
        } catch (java.io.IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Errore di rete durante la generazione AI della scheda", e);
        }
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(String rawJson) {
        Map<String, Object> root;
        try {
            root = objectMapper.readValue(rawJson, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Risposta di Groq non è JSON valido: " + rawJson, e);
        }


        Object errorField = root.get("error");
        if (errorField != null) {
            String errorMessage = extractErrorMessage(errorField);
            throw new RuntimeException("Groq ha restituito un errore: " + errorMessage);
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) root.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("Risposta di Groq senza 'choices': " + rawJson);
        }

        Map<String, Object> firstChoice = choices.get(0);
        Object finishReason = firstChoice.get("finish_reason");
        if ("length".equals(finishReason)) {
            throw new RuntimeException("La risposta di Groq è stata troncata (finish_reason=length): "
                    + "aumenta max_tokens o riduci il numero di esercizi/giorni richiesti");
        }

        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        String content = message != null ? (String) message.get("content") : null;
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Risposta di Groq senza contenuto testuale: " + rawJson);
        }
        return content;
    }

    @SuppressWarnings("unchecked")
    private String extractErrorMessage(Object errorField) {
        if (errorField instanceof Map) {
            Object msg = ((Map<String, Object>) errorField).get("message");
            if (msg != null) return msg.toString();
        }
        return String.valueOf(errorField);
    }

    private AIProgramResponseDTO parseResponse(String rawText) {
        String cleaned = rawText.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("^```(json)?", "").replaceAll("```$", "").trim();
        }


        try {
            Map<String, Object> asMap = objectMapper.readValue(cleaned, Map.class);
            if (asMap.containsKey("error")) {
                throw new RuntimeException("Il modello ha risposto con un errore invece della scheda: "
                        + extractErrorMessage(asMap.get("error")));
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception ignoredNotAMapOrNotJson) {
            // non è un semplice Map JSON generico: proseguiamo al parsing tipizzato
        }

        try {
            return objectMapper.readValue(cleaned, AIProgramResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Impossibile interpretare la scheda generata dall'AI: " + e.getMessage()
                    + " | contenuto ricevuto: " + cleaned, e);
        }
    }

    private Program mapToProgram(AIProgramResponseDTO parsed, GenerateProgramRequest request, User currentUser, List<Exercise> compatibleExercises) {
        Map<String, Exercise> byName = compatibleExercises.stream()
                .collect(Collectors.toMap(e -> e.getName().trim().toLowerCase(), e -> e, (a, b) -> a));

        Program program = new Program();
        program.setUser(currentUser);
        program.setGoal(request.goal());
        program.setStyle(request.style());
        program.setDaysPerWeek(request.daysPerWeek());
        program.setWeeksDuration(6);
        program.setName(parsed.programName() != null ? parsed.programName() : request.goal().toUpperCase() + " - " + request.style());
        program.setActive(true);

        List<ProgramDay> days = new ArrayList<>();
        int dayIndex = 0;
        for (AIProgramResponseDTO.AIProgramDayDTO aiDay : parsed.days()) {
            ProgramDay day = new ProgramDay();
            day.setProgram(program);
            day.setTitle(aiDay.title());
            day.setDayIndex(dayIndex++);

            List<ProgramExercise> dayExercises = new ArrayList<>();
            int orderIndex = 0;
            Set<String> seenInThisDay = new HashSet<>();

            for (AIProgramResponseDTO.AIProgramExerciseDTO aiEx : aiDay.exercises()) {
                if (aiEx.exerciseName() == null) continue;
                String key = aiEx.exerciseName().trim().toLowerCase();

                Exercise matched = byName.get(key);
                if (matched == null || seenInThisDay.contains(key)) {
                    continue;
                }
                seenInThisDay.add(key);

                ProgramExercise pe = new ProgramExercise();
                pe.setProgramDay(day);
                pe.setExercise(matched);
                pe.setSets(aiEx.sets() != null ? aiEx.sets() : 3);
                pe.setReps(aiEx.reps() != null ? aiEx.reps() : "8-12");
                pe.setRestSeconds(aiEx.restSeconds() != null ? aiEx.restSeconds() : 60);
                pe.setOrderIndex(orderIndex++);

                dayExercises.add(pe);
            }

            if (dayExercises.isEmpty()) {
                throw new RuntimeException("La scheda generata dall'AI non contiene esercizi validi per il giorno: " + aiDay.title());
            }

            day.setExercises(dayExercises);
            days.add(day);
        }

        if (days.isEmpty()) {
            throw new RuntimeException("La scheda generata dall'AI non contiene giorni validi");
        }

        program.setDays(days);
        return program;
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
}