package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingGatewayDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j(topic = "Gateway BookingController")
@Validated
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        BookingState state = BookingState.fromString(stateParam);
        log.info(String.format("Get Booking request by State: %s", stateParam));
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        BookingState state = BookingState.fromString(stateParam);
        log.info(String.format(
                "Get Booking request by State: %s for User with ID: %d", stateParam, userId));
        return bookingClient.getBookingsByOwner(userId, state, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PathVariable(name = "id") int bookingId) {
        log.info(String.format(
                "Get Booking request by ID: %d", bookingId));
        return bookingClient.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @Validated @RequestBody BookingGatewayDto dto) {
        log.info(String.format(
                "Create Booking request. Data: %s", dto));
        return bookingClient.createBooking(userId, dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> approve(@PathVariable("id") long id,
                                          @RequestParam boolean approved,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format(
                "Approve request for Booking with id: %d.", id));
        return bookingClient.approveBooking(userId, id, approved);
    }
}
