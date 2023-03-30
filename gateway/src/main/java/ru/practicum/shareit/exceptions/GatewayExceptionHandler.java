package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.shareit.exceptions.error.GatewayError;
import ru.practicum.shareit.exceptions.error.GatewaySingleError;

import java.util.List;

@Slf4j
@RestControllerAdvice("ru.practicum.shareit")
public class GatewayExceptionHandler {
    @ExceptionHandler(value = GatewayValidationException.class)
    public ResponseEntity<Object> handleValidationException(GatewayValidationException exception,
                                                            WebRequest request) {
        GatewayError error = GatewayError.builder()
                .status(HttpStatus.CONFLICT.value())
                .errors(List.of(exception.getMessage())).build();
        log.warn(String.format("WARNING: %s", error));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(value = GatewayUnsupportedStatus.class)
    public ResponseEntity<Object> handleUnsuportedStatusException(GatewayUnsupportedStatus exception,
                                                                  WebRequest request) {
        GatewaySingleError error = GatewaySingleError.builder()
                .state(HttpStatus.BAD_REQUEST.value())
                .error("Unknown state: UNSUPPORTED_STATUS")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
