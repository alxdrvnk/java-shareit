package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.State;
import ru.practicum.shareit.exceptions.ShareItBadRequest;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j(topic = "BookingController")
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
   private final BookingService bookingService;
   private final BookingMapper mapper;

   @PostMapping
   public BookingDto create(@Valid @RequestBody BookingRequestDto bookingDto,
                            @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "Create Booking request. Data: %s", bookingDto));
      validateEndBeforeStartDate(bookingDto);
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
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "Get Booking request by State: %s", state));
      return mapper.toBookingDtoList(
              bookingService.getAllByState(userId, State.fromString(state)));
   }

   @GetMapping("/owner")
   public Collection<BookingDto> getByOwnerWithSatet(@RequestParam(defaultValue = "ALL") String state,
                                                     @RequestHeader("X-Sharer-User-Id") long userId) {
      log.info(String.format(
              "Get Booking request by State: %s for User with ID: %d", state, userId));

      return mapper.toBookingDtoList(
              bookingService.getAllByOwnerWithState(userId, State.fromString(state)));
   }

   private void validateEndBeforeStartDate(BookingRequestDto dto) {
      if (dto.getEnd().isBefore(dto.getStart())) {
         throw new ShareItBadRequest("End date can't be before Start");
      }
   }
}
