package ru.practicum.shareit.booking.service.provider.impl;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.provider.BookingProvider;

import java.util.Collection;

@RequiredArgsConstructor
public class BookingProviderAllState implements BookingProvider {

    private final BookingRepository bookingRepository;

    @Override
    public Collection<Booking> getBookingsOfUser(Long userId, String state) {
        return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
    }

    @Override
    public Collection<Booking> getBookingsOfOwnerItems(Long userId, String state) {
        return bookingRepository.findAllByOwnerId(userId);
    }
}
