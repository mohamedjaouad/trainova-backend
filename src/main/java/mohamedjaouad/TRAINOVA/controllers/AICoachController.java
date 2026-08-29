package mohamedjaouad.TRAINOVA.controllers;

import jakarta.validation.Valid;
import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.recordsDTO.CoachChatRequest;
import mohamedjaouad.TRAINOVA.recordsDTO.CoachChatResponse;
import mohamedjaouad.TRAINOVA.services.AICoachChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-coach")
public class AICoachController {
    private final AICoachChatService coachChatService;

    public AICoachController(AICoachChatService coachChatService) {
        this.coachChatService = coachChatService;
    }

    @PostMapping("/chat")
    public CoachChatResponse chat(
            @RequestBody @Valid CoachChatRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return coachChatService.reply(request.message(), currentUser);
    }
}
