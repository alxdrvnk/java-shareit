package ru.practicum.shareit.booking.dto;


import ru.practicum.shareit.exceptions.GatewayUnsupportedStatus;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState fromString(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new GatewayUnsupportedStatus(
                    String.format("Booking doesn't support '%s' state", state));
        }
    }
}