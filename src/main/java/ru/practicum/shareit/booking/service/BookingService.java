package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {

    Booking create(BookingRequestDto toBooking, long userId);

    Booking approve(long id, long userId, boolean approved);

    Booking get(long userId, long id);

    Collection<Booking> getAllByState(long userId, BookingState bookingState, int from, int size);

    Collection<Booking> getAllByOwnerWithState(long userId, BookingState bookingState, int from, int size);
}
