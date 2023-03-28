package ru.practicum.shareit.booking.service.provider.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.provider.BookingProvider;

import java.util.Collection;

@RequiredArgsConstructor
public class BookingProviderRejectedState implements BookingProvider {

    private final BookingRepository bookingRepository;

    @Override
    public Collection<Booking> getBookingsOfUser(Long userId, String state, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId, BookingStatus.REJECTED, pageable);
    }

    @Override
    public Collection<Booking> getBookingsOfOwnerItems(Long userId, String state, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.REJECTED, pageable);
    }
}
