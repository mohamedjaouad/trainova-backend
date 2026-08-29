package mohamedjaouad.TRAINOVA.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;


@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(mohamedjaouad.TRAINOVA.exceptions.NotFoundException.class)
    public ResponseEntity<mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload> handleNotFound(mohamedjaouad.TRAINOVA.exceptions.NotFoundException ex) {
        return new ResponseEntity<>(new mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload(ex.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(new mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload(ex.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(mohamedjaouad.TRAINOVA.exceptions.UnauthorizedException.class)
    public ResponseEntity<mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload> handleUnauthorized(mohamedjaouad.TRAINOVA.exceptions.UnauthorizedException ex) {
        return new ResponseEntity<>(new mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload(ex.getMessage(), LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(new mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload("Errori di validazione", LocalDateTime.now(), errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload> handleGeneric(Exception ex) {
        return new ResponseEntity<>(new mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload("Errore interno del server: " + ex.getMessage(), LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
