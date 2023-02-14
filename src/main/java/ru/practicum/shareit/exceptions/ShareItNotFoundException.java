package ru.practicum.shareit.exceptions;

public class ShareItNotFoundException extends RuntimeException {
    public ShareItNotFoundException(String message) {
        super(message);
    }
}
