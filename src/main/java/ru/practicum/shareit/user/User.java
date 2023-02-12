package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * TODO Sprint add-controllers.
 */

@Value
@Builder
public class User {

    @With
    Long id;

    String name;

    String email;
}