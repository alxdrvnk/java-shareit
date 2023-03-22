package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemWithBookingsRepository {
    List<ItemResponseDto> itemsWithNextAndPrevBookings(long userId, LocalDateTime date, Long itemId);
}
