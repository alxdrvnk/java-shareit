package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingForItem;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemForBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Component
public class BookingMapper {

    public Booking toBooking(BookingRequestDto bookingRequestDto, Item item, User user, BookingStatus status) {
        if (bookingRequestDto == null && item == null && user == null && status == null) {
            return null;
        }

        Booking.BookingBuilder booking = Booking.builder();

        if (bookingRequestDto != null) {
            booking.start(fromInstant(bookingRequestDto.getStart()));
            booking.end(fromInstant(bookingRequestDto.getEnd()));
        }
        booking.item(item);
        booking.booker(user);
        booking.status(status);

        return booking.build();
    }

    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDto.BookingDtoBuilder bookingDto = BookingDto.builder();

        bookingDto.id(booking.getId());
        bookingDto.start(fromInstant(booking.getStart()));
        bookingDto.end(fromInstant(booking.getEnd()));
        bookingDto.item(itemToItemForBookingDto(booking.getItem()));
        bookingDto.booker(booking.getBooker());
        bookingDto.status(booking.getStatus());

        return bookingDto.build();
    }

    public Collection<BookingDto> toBookingDtoList(Collection<Booking> allByState) {
        if (allByState == null) {
            return Collections.emptyList();
        }

        Collection<BookingDto> collection = new ArrayList<>(allByState.size());
        for (Booking booking : allByState) {
            collection.add(toBookingDto(booking));
        }

        return collection;
    }

    public BookingItemDto toBookingItemDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingItemDto.BookingItemDtoBuilder bookingItemDto = BookingItemDto.builder();

        bookingItemDto.bookerId(bookingBookerId(booking));
        bookingItemDto.startDate(fromInstant(booking.getStart()));
        bookingItemDto.endDate(fromInstant(booking.getEnd()));
        bookingItemDto.id(booking.getId());

        return bookingItemDto.build();
    }

    public BookingItemDto toBookingItemDto(BookingForItem booking) {
        if (booking == null) {
            return null;
        }

        BookingItemDto.BookingItemDtoBuilder bookingItemDto = BookingItemDto.builder();

        bookingItemDto.bookerId(booking.getBookerId());
        bookingItemDto.startDate(fromInstant(booking.getStartDate()));
        bookingItemDto.endDate(fromInstant(booking.getEndDate()));
        bookingItemDto.id(booking.getId());

        return bookingItemDto.build();
    }


    protected ItemForBookingDto itemToItemForBookingDto(Item item) {
        if (item == null) {
            return null;
        }

        ItemForBookingDto.ItemForBookingDtoBuilder itemForBookingDto = ItemForBookingDto.builder();

        itemForBookingDto.id(item.getId());
        itemForBookingDto.name(item.getName());

        return itemForBookingDto.build();
    }

    private Long bookingBookerId(Booking booking) {
        User booker = booking.getBooker();
        if (booker == null) {
            return null;
        }
        return booker.getId();
    }


    private LocalDateTime fromInstant(LocalDateTime instant) {
        return instant == null ? null : instant.truncatedTo(ChronoUnit.SECONDS);
    }
}
