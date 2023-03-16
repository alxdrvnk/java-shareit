package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ShareItBadRequest;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

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
        userService.getUserBy(userId);
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Booking with Id: %d not found", id)));

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ShareItNotFoundException(
                    String.format("User with Id: %d is not owner or booker Item with Id: %d", userId, id));
        }

        return booking;
    }

    @Override
    public List<Booking> getAllByState(long userId, State state) {
        userService.getUserBy(userId);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId, BookingStatus.REJECTED);
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<Booking> getAllByOwnerWithState(long userId, State state) {
        userService.getUserBy(userId);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByOwnerId(userId);
            case PAST:
                return bookingRepository.findAllByOwnerIdAndPastState(userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByOwnerIdAndFutureState(userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByOwnerIdAndCurrentState(
                        userId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.REJECTED);
            default:
                return Collections.emptyList();
        }
    }
}
