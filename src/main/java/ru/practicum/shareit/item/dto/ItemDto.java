package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Value
@Builder
public class ItemDto {

    Long id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    Boolean available;

    User owner;

    ItemRequest request;
}
