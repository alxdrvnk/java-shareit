package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.exceptions.ShareItBadRequest;

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
             throw new ShareItBadRequest(
                     String.format("Test get user bookings by 'UNSUPPORTED_STATUS' state"));
         }
    }
}
