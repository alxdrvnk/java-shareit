package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ItemForItemRequestDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
    Long ownerId;
}
