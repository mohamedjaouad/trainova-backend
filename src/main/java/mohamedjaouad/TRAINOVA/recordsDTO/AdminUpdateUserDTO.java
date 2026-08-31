package mohamedjaouad.TRAINOVA.recordsDTO;

public record AdminUpdateUserDTO(
        String fullName,
        String email,
        Boolean isAdmin
) {}