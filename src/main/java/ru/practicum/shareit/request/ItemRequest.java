package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Value
@Builder
public class ItemRequest {
    Long id;
    String description;
    User requestor;
    LocalDateTime created;
}
