package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booker", source = "user")
    Booking toBooking(BookingRequestDto bookingRequestDto, Item item, User user, BookingStatus status);

    BookingDto toBookingDto(Booking booking);

    Collection<BookingDto> toBookingDtoList(List<Booking> allByState);

    @Mapping(target = "bookerId", source = "booker.id")
    @Mapping(target = "startDate", source = "start")
    @Mapping(target = "endDate", source = "end")
    BookingItemDto toBookingItemDto(Booking booking);

}
