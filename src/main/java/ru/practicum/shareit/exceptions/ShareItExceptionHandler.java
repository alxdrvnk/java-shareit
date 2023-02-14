package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.shareit.exceptions.error.ShareItError;

import java.util.List;

@Slf4j
@ControllerAdvice
public class ShareItExceptionHandler {

    @ExceptionHandler(value = ShareItNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ShareItNotFoundException exception,
                                                          WebRequest request) {
        ShareItError error = ShareItError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .errors(List.of(exception.getMessage())).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(value = ShareItValidationException.class)
    public ResponseEntity<Object> handleValidationException(ShareItValidationException exception,
                                                            WebRequest request) {
        ShareItError error = ShareItError.builder()
                .status(HttpStatus.CONFLICT.value())
                .errors(List.of(exception.getMessage())).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(value = ShareItAlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(ShareItAlreadyExistsException exception,
                                                               WebRequest request) {
        ShareItError error = ShareItError.builder()
                .status(HttpStatus.CONFLICT.value())
                .errors(List.of(exception.getMessage())).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}