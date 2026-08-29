package mohamedjaouad.TRAINOVA.recordsDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SaveWorkoutExerciseRequest(
        @NotNull UUID exerciseId,
        @NotNull @Min(1) Integer sets,
        @NotBlank String reps,
        Double weightKg,
        String rest
) {}
