package mohamedjaouad.TRAINOVA.recordsDTO;

import java.time.LocalDate;
import java.util.UUID;


public record UserProfileDTO(
        UUID id,
        String username,
        String email,
        String fullName,
        String avatarUrl,
        LocalDate dateOfBirth,
        Double heightCm,
        Double weightKg,
        Integer trainingExperienceYears,
        String trainingStyle,
        Integer daysPerWeek,
        Integer level,
        Integer xp
) {
}
