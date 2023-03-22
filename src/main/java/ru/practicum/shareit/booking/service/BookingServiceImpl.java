package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.provider.BookingProviderSelector;
import ru.practicum.shareit.exceptions.ShareItBadRequest;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingProviderSelector bookingProviderSelector;
    private final BookingMapper mapper;

    @Override
    public Booking create(BookingRequestDto bookingDto, long userId) {
        User user = userService.getUserBy(userId);
        Item item = itemService.getItemNotOwnedByUser(userId, bookingDto.getItemId());
        if (bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new ShareItBadRequest("Start date and End date can't be equal");
        }
        if (!item.isAvailable()) {
            throw new ShareItBadRequest(String.format("Item with Id: %d doesn't available", item.getId()));
        }

        Booking booking = mapper.toBooking(bookingDto, item, user, BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking approve(long id, long userId, boolean approved) {
        User user = userService.getUserBy(userId);
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ShareItNotFoundException(String.format("Booking with Id:%s not found", id)));

        if (!booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new ShareItNotFoundException(
                    String.format("User with Id: %d is not owner Item with Id: %d", userId, booking.getItem().getId()));
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED) && approved) {
            throw new ShareItBadRequest(
                    String.format("Booking with Id: %d already approved", id));
        }

        if (approved) {
            booking = booking.withStatus(BookingStatus.APPROVED);
        } else {
            booking = booking.withStatus(BookingStatus.REJECTED);
        }

        return bookingRepository.save(booking);
    }

    @Override
    public Booking get(long userId, long id) {
        return bookingRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("User with Id: %d is not owner or booker Item with Id: %d", userId, id)));
    }

    @Override
    public Collection<Booking> getAllByState(long userId, BookingState bookingState) {
        Collection<Booking> bookings = bookingProviderSelector.getBookingsOfUser(userId, bookingState.name());
        if (bookings.isEmpty()) {
            throw new ShareItNotFoundException(
                    String.format("User with Id: %d not found or doesn't have bookings", userId));
        }
        return bookings;
    }

    @Override
    public Collection<Booking> getAllByOwnerWithState(long userId, BookingState bookingState) {
        Collection<Booking> bookings = bookingProviderSelector.getBookingsOfOwnerItems(userId, bookingState.name());
        if (bookings.isEmpty()) {
            throw new ShareItNotFoundException(
                    String.format("User with Id: %d not found or is not owner", userId));
        }
        return bookings;
    }
}
