package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;
import java.util.Optional;

public interface ItemRequestsWithItemsRepository {

    List<ItemRequestResponseDto> getByRequesterId(Long userId);

    Optional<ItemRequestResponseDto> getItemRequestById(Long requestId);

    List<ItemRequestResponseDto> findByRequesterIdNot(long userId, int from, int size);
}
