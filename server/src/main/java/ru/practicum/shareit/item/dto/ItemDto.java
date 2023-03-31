package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class ItemDto {

    Long id;

    String name;

    String description;

    Boolean available;

    User owner;

    Long requestId;
}
