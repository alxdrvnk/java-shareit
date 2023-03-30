package ru.practicum.shareit.exceptions;

public class GatewayUnsupportedStatus extends RuntimeException {

    public GatewayUnsupportedStatus(String message) {
        super(message);
    }
}