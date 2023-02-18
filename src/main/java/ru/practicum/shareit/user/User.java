package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
public class User {

    @With
    long id;

    String name;

    String email;
}