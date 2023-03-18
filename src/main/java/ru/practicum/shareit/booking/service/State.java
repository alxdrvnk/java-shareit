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
                     "Test get user bookings by 'UNSUPPORTED_STATUS' state");
         }
    }
}
