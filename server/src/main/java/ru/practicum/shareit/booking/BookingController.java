package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingState;

import java.util.Collection;

@Slf4j(topic = "BookingController")
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
   private final BookingService bookingService;
   private final BookingMapper mapper;

   @PostMapping
   public BookingDto create(@RequestBody BookingRequestDto bookingDto,
                            @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "Create Booking request. Data: %s", bookingDto));
      return mapper.toBookingDto(
              bookingService.create(bookingDto, userId));
   }

   @PatchMapping("/{id}")
   public BookingDto approve(@PathVariable("id") long id,
                             @RequestParam boolean approved,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "Approve request for Booking with id: %d.", id));
      return mapper.toBookingDto(
              bookingService.approve(id, userId, approved));
   }

   @GetMapping("/{id}")
   public BookingDto get(@PathVariable("id") long id,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "Get Booking request by ID: %d", id));
      return mapper.toBookingDto(
              bookingService.get(userId, id));

   }

   @GetMapping
   public Collection<BookingDto> getByState(@RequestParam(defaultValue = "ALL") String state,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "20") int size,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "Get Booking request by State: %s", state));
      return mapper.toBookingDtoList(
              bookingService.getAllByState(userId, BookingState.valueOf(state), from, size));
   }

   @GetMapping("/owner")
   public Collection<BookingDto> getByOwnerWithState(@RequestParam(defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "20") int size,
                                                     @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "Get Booking request by State: %s for User with ID: %d", state, userId));
      return mapper.toBookingDtoList(
              bookingService.getAllByOwnerWithState(userId, BookingState.valueOf(state), from, size));
   }
}
