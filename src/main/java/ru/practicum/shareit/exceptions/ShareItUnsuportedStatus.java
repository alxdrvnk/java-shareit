package ru.practicum.shareit.exceptions;

public class ShareItUnsuportedStatus extends RuntimeException{

    public ShareItUnsuportedStatus(String message) {
        super(message);
    }
}
