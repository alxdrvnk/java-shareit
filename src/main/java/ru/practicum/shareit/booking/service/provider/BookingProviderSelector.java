package ru.practicum.shareit.booking.service.provider;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingState;
import ru.practicum.shareit.booking.service.provider.impl.*;

import java.time.Clock;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Component
public class BookingProviderSelector implements BookingProvider {

    private final Map<BookingState, BookingProvider> providers;

    public BookingProviderSelector(BookingRepository bookingRepository, Clock clock) {
        providers = new EnumMap<>(BookingState.class);
        providers.put(BookingState.ALL, new BookingProviderAllState(bookingRepository));
        providers.put(BookingState.PAST, new BookingProviderPastState(clock, bookingRepository));
        providers.put(BookingState.FUTURE, new BookingProviderFutureState(clock, bookingRepository));
        providers.put(BookingState.CURRENT, new BookingProviderCurrentState(clock, bookingRepository));
        providers.put(BookingState.WAITING, new BookingProviderWaitingState(bookingRepository));
        providers.put(BookingState.REJECTED, new BookingProviderRejectedState(bookingRepository));
    }

    @Override
    public Collection<Booking> getBookingsOfUser(Long userId, String state) {
        BookingProvider provider = providers.get(BookingState.valueOf(state));
        return provider.getBookingsOfUser(userId, state);
    }

    @Override
    public Collection<Booking> getBookingsOfOwnerItems(Long userId, String state) {
        BookingProvider provider = providers.get(BookingState.valueOf(state));
        return provider.getBookingsOfOwnerItems(userId, state);
    }
}
