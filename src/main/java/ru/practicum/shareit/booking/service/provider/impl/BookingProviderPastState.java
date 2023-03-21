package ru.practicum.shareit.booking.service.provider.impl;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.provider.BookingProvider;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
public class BookingProviderPastState implements BookingProvider {

    private final BookingRepository bookingRepository;
    private final Clock clock = Clock.systemUTC();

    @Override
    public Collection<Booking> getBookingsOfUser(Long userId, String state) {
        return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(clock));
    }

    @Override
    public Collection<Booking> getBookingsOfOwnerItems(Long userId, String state) {
        return bookingRepository.findAllByOwnerIdAndPastState(userId, LocalDateTime.now(clock));
    }
}
