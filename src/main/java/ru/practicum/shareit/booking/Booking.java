package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Value
@Builder
public class Booking {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    BookingSatus status;
}
