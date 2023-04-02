package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exceptions.error.ShareItError;

import java.util.List;

@Slf4j
@ControllerAdvice
public class ShareItExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ShareItNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ShareItNotFoundException exception,
                                                          WebRequest request) {
        ShareItError error = ShareItError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .errors(List.of(exception.getMessage())).build();
        log.warn(String.format("WARNING: %s", error));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(value = ShareItAlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(ShareItAlreadyExistsException exception,
                                                               WebRequest request) {
        ShareItError error = ShareItError.builder()
                .status(HttpStatus.CONFLICT.value())
                .errors(List.of(exception.getMessage())).build();
        log.warn(String.format("WARNING: %s", error));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(value = ShareItBadRequest.class)
    public ResponseEntity<Object> handleBadRequestException(ShareItBadRequest exception,
                                                               WebRequest request) {
        ShareItError error = ShareItError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(List.of(exception.getMessage())).build();
        log.warn(String.format("WARNING: %s", error));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}