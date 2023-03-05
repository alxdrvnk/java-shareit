package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.State;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
   private final BookingService bookingService;

   @PostMapping
   public BookingDto create(@Valid @RequestBody BookingRequestDto bookingDto,
                            @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "BookingController: create Booking request. Data: %s", bookingDto));
      return BookingMapper.MAPPER.toBookingDto(
              bookingService.create(bookingDto, userId));
   }

   @PatchMapping("/{id}")
   public BookingDto approve(@PathVariable("id") long id,
                             @RequestParam boolean approved,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "BookingController: approve request for Booking with id: %d.", id));
      return BookingMapper.MAPPER.toBookingDto(
              bookingService.approve(id, userId, approved));
   }

   @GetMapping("/{id}")
   public BookingDto get(@PathVariable("id") long id,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "BookingController: get Booking request by ID: %d", id));
      return BookingMapper.MAPPER.toBookingDto(
              bookingService.get(userId, id));

   }

   @GetMapping
   public Collection<BookingDto> getByState(@RequestParam(defaultValue = "ALL") String state,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "BookingController: get Booking request by State: %s", state));
      return BookingMapper.MAPPER.toBookingDtoList(
              bookingService.getAllByState(userId, State.valueOf(state)));
   }

   @GetMapping("/owner")
   public Collection<BookingDto> getByOwnerWithSatet(@RequestParam String state,
                                                     @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "BookingController: get Booking request by State: %s for User with ID: %d", state, userId));

      return BookingMapper.MAPPER.toBookingDtoList(
              bookingService.getAllByOwnerWithState(userId, State.fromString(state)));
   }

}
