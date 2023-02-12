package ru.practicum.shareit.exceptions;

public class ShareitNotFoundException extends RuntimeException {
    public ShareitNotFoundException(String message) {
        super(message);
    }
}
