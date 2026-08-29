package mohamedjaouad.TRAINOVA.recordsDTO;

import jakarta.validation.constraints.NotBlank;

public record CoachChatRequest(@NotBlank String message) {}
