package ru.practicum.shareit.booking.service.provider;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingProvider {
    Collection<Booking> getBookingsOfUser(Long userId, String state);

    Collection<Booking> getBookingsOfOwnerItems(Long userId, String state);
}
