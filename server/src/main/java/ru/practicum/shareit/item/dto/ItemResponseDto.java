package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
@Jacksonized
public class ItemResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;
    User owner;
    Long requestId;
    @With
    List<CommentResponseDto> comments;
    BookingItemDto lastBooking;
    BookingItemDto nextBooking;
}
