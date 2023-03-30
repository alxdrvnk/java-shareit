package ru.practicum.shareit.booking.dto;


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
            throw new ShareItUnsupportedStatus(
                    String.format("Booking doesn't support '%s' state", state));
        }
    }
}