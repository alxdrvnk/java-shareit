package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Value
@Builder
public class ItemDto {

    Long id;

    String name;

    String description;

    boolean available;

    User owner;

    ItemRequest request;
}
