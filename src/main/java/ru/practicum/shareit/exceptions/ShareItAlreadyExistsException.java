package ru.practicum.shareit.exceptions;

public class ShareItAlreadyExistsException extends RuntimeException {
    public ShareItAlreadyExistsException(String message) {
        super(message);
    }
}
