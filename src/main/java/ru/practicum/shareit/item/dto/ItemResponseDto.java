package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
@Jacksonized
public class ItemResponseDto {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    Boolean available;
    User owner;
    ItemRequest request;
    @With
    List<CommentResponseDto> comments;
    BookingItemDto lastBooking;
    BookingItemDto nextBooking;
}
