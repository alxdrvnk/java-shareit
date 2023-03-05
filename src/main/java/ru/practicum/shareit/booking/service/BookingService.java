package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking create(BookingRequestDto toBooking, long userId);

    Booking approve(long id, long userId, boolean approved);

    Booking get(long userId, long id);

    List<Booking> getAllByState(long userId, State state);

    List<Booking> getAllByOwnerWithState(long userId, State state);
}
