package ru.practicum.shareit.exceptions;

public class GatewayValidationException extends RuntimeException {
    public GatewayValidationException(String message) {
        super(message);
    }
}