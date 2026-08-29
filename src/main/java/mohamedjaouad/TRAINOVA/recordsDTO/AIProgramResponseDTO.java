package mohamedjaouad.TRAINOVA.recordsDTO;

import java.util.List;

public record AIProgramResponseDTO(
        String programName,
        List<AIProgramDayDTO> days
) {
    public record AIProgramDayDTO(
            String title,
            List<AIProgramExerciseDTO> exercises
    ) {}

    public record AIProgramExerciseDTO(
            String exerciseName,
            Integer sets,
            String reps,
            Integer restSeconds
    ) {}
}