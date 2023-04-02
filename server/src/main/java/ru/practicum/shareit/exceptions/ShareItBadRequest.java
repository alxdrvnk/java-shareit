package ru.practicum.shareit.exceptions;

public class ShareItBadRequest extends RuntimeException {
    public ShareItBadRequest(String message) {
        super(message);
    }
}
