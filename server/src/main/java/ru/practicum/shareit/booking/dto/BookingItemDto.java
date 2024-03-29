package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class BookingItemDto {
    Long id;
    Long bookerId;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
