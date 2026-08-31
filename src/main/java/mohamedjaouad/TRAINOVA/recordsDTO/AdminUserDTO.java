package mohamedjaouad.TRAINOVA.recordsDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminUserDTO(
        UUID id,
        String username,
        String email,
        String fullName,
        boolean isAdmin,
        LocalDateTime createdAt
) {}