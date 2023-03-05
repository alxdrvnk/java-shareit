package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.exceptions.ShareItValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public Booking create(BookingRequestDto bookingDto, long userId) {
        User user = userService.getUserBy(userId);
        Item item = itemService.getItemNotOwnedByUser(userId, bookingDto.getItemId());

        if (!item.isAvailable()) {
            throw new ShareItValidationException(String.format("Item with Id: %d doesn't available", item.getId()));
        }

        Booking booking = BookingMapper.MAPPER.toBooking(bookingDto, item, user, BookingStatus.WAITING);
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
            throw new ShareItValidationException(
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
        return bookingRepository.findAllByBookerIdAndStatusIs(userId, state.name());
    }

    @Override
    public List<Booking> getAllByOwnerWithState(long userId, State state) {
        userService.getUserBy(userId);
        return null;
//        return bookingRepository.findAllByOwnerIdAndStatus(userId, state.name());
    }
}
