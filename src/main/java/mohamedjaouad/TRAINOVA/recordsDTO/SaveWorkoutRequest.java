package mohamedjaouad.TRAINOVA.recordsDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SaveWorkoutRequest(
        @NotBlank String title,
        @NotBlank String type,
        String intensity,
        Integer duration,
        String notes,
        @NotEmpty List<@Valid SaveWorkoutExerciseRequest> exercises
) {}
