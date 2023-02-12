package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Value
@Builder
public class ItemRequestDto {
    Long id;
    String description;
    User requestor;
    LocalDateTime created;
}
