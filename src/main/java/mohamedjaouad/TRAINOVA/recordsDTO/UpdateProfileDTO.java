package mohamedjaouad.TRAINOVA.recordsDTO;

import java.time.LocalDate;


public record UpdateProfileDTO(
        String fullName,
        LocalDate dateOfBirth,
        Double heightCm,
        Double weightKg,
        Integer trainingExperienceYears,
        String trainingStyle,
        Integer daysPerWeek
) {
}
