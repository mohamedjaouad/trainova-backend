package mohamedjaouad.TRAINOVA.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import mohamedjaouad.TRAINOVA.entities.Program;
import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.recordsDTO.CoachChatResponse;
import mohamedjaouad.TRAINOVA.repositories.ProgramRepository;
import mohamedjaouad.TRAINOVA.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class AICoachChatService {

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "openai/gpt-oss-120b";

    private final ObjectMapper objectMapper;
    private final WorkoutRepository workoutRepository;
    private final ProgramRepository programRepository;
    private final HttpClient httpClient;
    private final String apiKey;

    public AICoachChatService(
            ObjectMapper objectMapper,
            WorkoutRepository workoutRepository,
            ProgramRepository programRepository,
            @Value("${groq.api.key:}") String apiKey
    ) {
        this.objectMapper = objectMapper;
        this.workoutRepository = workoutRepository;
        this.programRepository = programRepository;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
    }

    public CoachChatResponse reply(String message, User user) {
        String context = buildContext(user);
        if (apiKey == null || apiKey.isBlank()) {
            return new CoachChatResponse(fallbackReply(message, context), false);
        }

        try {
            String body = objectMapper.writeValueAsString(Map.of(
                    "model", MODEL,
                    "messages", List.of(
                            Map.of("role", "system", "content", "Sei Trainova Coach, un coach fitness prudente e concreto. Rispondi in italiano in modo conciso. Non dare diagnosi mediche. Contesto utente: " + context),
                            Map.of("role", "user", "content", message)
                    ),
                    "temperature", 0.6
            ));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GROQ_API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Groq ha risposto con stato " + response.statusCode());
            }
            return new CoachChatResponse(extractContent(response.body()), true);
        } catch (Exception ignored) {
            return new CoachChatResponse(fallbackReply(message, context), false);
        }
    }

    private String buildContext(User user) {
        long completedWorkouts = workoutRepository.countByUserId(user.getId());
        List<Program> activePrograms = programRepository.findByUserIdAndActiveTrue(user.getId());
        String program = activePrograms.isEmpty() ? "nessun programma attivo" : activePrograms.getFirst().getName();
        return "Utente: " + user.getUsername() + "; stile: " + (user.getTrainingStyle() == null ? "non impostato" : user.getTrainingStyle())
                + "; allenamenti completati: " + completedWorkouts + "; programma attivo: " + program + ".";
    }

    @SuppressWarnings("unchecked")
    private String extractContent(String rawJson) throws Exception {
        Map<String, Object> root = objectMapper.readValue(rawJson, Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) root.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IllegalStateException("Risposta AI senza choices");
        }
        Map<String, Object> message = (Map<String, Object>) choices.getFirst().get("message");
        String content = (String) message.get("content");
        if (content == null || content.isBlank()) {
            throw new IllegalStateException("Risposta AI vuota");
        }
        return content.trim();
    }

    private String fallbackReply(String message, String context) {
        String normalized = message.toLowerCase();
        if (normalized.contains("dolore") || normalized.contains("infortun")) {
            return "Se avverti dolore acuto, interrompi l'esercizio e consulta un professionista sanitario. Per un fastidio lieve, riduci carico e volume e cura la tecnica.";
        }
        if (normalized.contains("scheda") || normalized.contains("programma")) {
            return "Il tuo contesto attuale è: " + context + " Vai in “Genera scheda” per creare o aggiornare il tuo programma; posso poi aiutarti ad adattare volume e recuperi.";
        }
        return "Posso aiutarti con tecnica, progressione, recupero e alimentazione generale. " + context + " Dimmi il tuo obiettivo o l'esercizio su cui vuoi lavorare.";
    }
}
