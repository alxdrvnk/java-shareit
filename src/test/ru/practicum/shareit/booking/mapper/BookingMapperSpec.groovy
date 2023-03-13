package ru.practicum.shareit.booking.mapper

import ru.practicum.shareit.booking.dto.BookingItemDto
import ru.practicum.shareit.booking.dto.BookingRequestDto
import ru.practicum.shareit.booking.model.Booking
import ru.practicum.shareit.booking.model.BookingStatus
import ru.practicum.shareit.item.model.Item
import ru.practicum.shareit.user.model.User
import spock.lang.Specification

import java.time.LocalDateTime

class BookingMapperSpec extends Specification {

    BookingMapper mapper = new BookingMapperImpl();

    def "Should map Booking to BookingDto" () {
        given:
        User owner = User.builder()
        .id(1)
        .name("User1")
        .email("mail@mail.mail").build()

        User booker = User.builder()
        .id(2)
        .name("User2")
        .email("mail2@mail.mail").build()

        Item item = Item.builder()
        .id(1L)
        .name("Item")
        .description("Item")
        .available(true)
        .owner(owner).build()

        Booking booking = Booking.builder()
        .id(1L)
        .start(LocalDateTime.of(2007, 9, 1,12,0,0))
        .end(LocalDateTime.of(2007,10,1,12,0,0))
        .item(item)
        .booker(booker)
        .status(BookingStatus.APPROVED)
        .build()

        when:
        def dto = mapper.toBookingDto(booking)

        then:
        dto.id == 1
        dto.start == LocalDateTime.of(2007, 9, 1,12,0,0)
        dto.end == LocalDateTime.of(2007,10,1,12,0,0)
        dto.item == item
        dto.booker == booker
        dto.status == BookingStatus.APPROVED
    }

    def "Should map Booking to BookingItemDto" () {
        given:
        User owner = User.builder()
                .id(1)
                .name("User1")
                .email("mail@mail.mail").build()

        User booker = User.builder()
                .id(2)
                .name("User2")
                .email("mail2@mail.mail").build()

        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Item")
                .available(true)
                .owner(owner).build()

        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2007, 9, 1,12,0,0))
                .end(LocalDateTime.of(2007,10,1,12,0,0))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build()

        when:
        BookingItemDto dto = mapper.toBookingItemDto(booking);

        then:
        dto.id == 1
        dto.bookerId == 2
        dto.startDate == LocalDateTime.of(2007, 9, 1,12,0,0)
        dto.endDate == LocalDateTime.of(2007,10,1,12,0,0)
    }

    def "Should map BookingRequestDto to Booking" () {
        given:
        User owner = User.builder()
                .id(1)
                .name("User1")
                .email("mail@mail.mail").build()

        User booker = User.builder()
                .id(2)
                .name("User2")
                .email("mail2@mail.mail").build()

        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Item")
                .available(true)
                .owner(owner).build()

        BookingRequestDto bookingRequest = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2007, 9, 1,12,0,0))
                .end(LocalDateTime.of(2007,10,1,12,0,0))
                .build()
        when:
        def dto = mapper.toBooking(bookingRequest, item, booker, BookingStatus.APPROVED)

        then:
        dto.id == null
        dto.start == LocalDateTime.of(2007, 9, 1,12,0,0)
        dto.end == LocalDateTime.of(2007,10,1,12,0,0)
        dto.item == item
        dto.booker == booker
        dto.status == BookingStatus.APPROVED
    }
}
