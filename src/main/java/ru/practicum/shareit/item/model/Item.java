package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Value
@Builder
public class Item {

    @With
    Long id;

    String name;

    String description;

    boolean available;

    @With
    User owner;

    ItemRequest request;
}
