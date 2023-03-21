package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.exceptions.ShareItUnsupportedStatus;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State fromString(String state) {
         try {
             return State.valueOf(state);
         } catch (IllegalArgumentException e) {
             throw new ShareItUnsupportedStatus(
                     String.format("Booking doesn't support '%s' state",state));
         }
    }
}
