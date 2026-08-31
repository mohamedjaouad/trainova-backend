package mohamedjaouad.TRAINOVA.recordsDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "L'username è obbligatorio")
        @Size(min = 3, max = 20, message = "L'username deve avere tra 3 e 20 caratteri")
        @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9._]{1,18}[a-zA-Z0-9]$", message = "Username non valido: solo lettere, numeri, punti e underscore")
        String username,

        @NotBlank(message = "Il nome completo è obbligatorio")
        @Size(min = 2, max = 60, message = "Il nome deve avere tra 2 e 60 caratteri")
        String fullName,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Formato email non valido")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).+$",
                message = "La password deve contenere almeno un numero e un carattere speciale"
        )
        String password,

        boolean isAdmin
) {}