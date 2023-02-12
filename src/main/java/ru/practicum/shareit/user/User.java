package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Value;

/**
 * TODO Sprint add-controllers.
 */

@Value
@Builder
public class User {
    Long id;
    String name;
    String email;
}
