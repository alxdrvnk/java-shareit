package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@Jacksonized
public class ItemRequestResponseDto {
    Long id;
    String description;
    LocalDateTime created;

    @With
    List<ItemForItemRequestDto> items;
}
