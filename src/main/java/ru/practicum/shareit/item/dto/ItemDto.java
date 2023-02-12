package ru.practicum.shareit.item.dto;

import lombok.Value;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Value
public class ItemDto {
    Long id;
    String name;
    String description;
    boolean available;
    User owner;
}
