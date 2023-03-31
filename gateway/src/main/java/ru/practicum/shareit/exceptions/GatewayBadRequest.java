package ru.practicum.shareit.exceptions;

public class GatewayBadRequest extends RuntimeException{

    public GatewayBadRequest(String message) {
        super(message);
    }
}
