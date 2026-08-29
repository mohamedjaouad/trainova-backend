package mohamedjaouad.TRAINOVA.exceptions;

import java.time.LocalDateTime;
import java.util.List;


public record ErrorsPayload(String message, LocalDateTime timestamp, List<String> errors) {
    public ErrorsPayload(String message, LocalDateTime timestamp) {
        this(message, timestamp, null);
    }
}
