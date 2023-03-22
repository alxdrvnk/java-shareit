package ru.practicum.shareit.booking.service.provider.impl;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.provider.BookingProvider;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
public class BookingProviderCurrentState implements BookingProvider {

    private final Clock clock;
    private final BookingRepository bookingRepository;

    @Override
    public Collection<Booking> getBookingsOfUser(Long userId, String state) {
        return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                userId, LocalDateTime.now(clock), LocalDateTime.now(clock));
    }

    @Override
    public Collection<Booking> getBookingsOfOwnerItems(Long userId, String state) {
        return bookingRepository.findAllByOwnerIdAndCurrentState(
                userId, LocalDateTime.now(clock));
    }
}
