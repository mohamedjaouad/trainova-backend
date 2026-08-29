package mohamedjaouad.TRAINOVA.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(UUID id) {
        super("Utente con id " + id + " non trovato");
    }
}
