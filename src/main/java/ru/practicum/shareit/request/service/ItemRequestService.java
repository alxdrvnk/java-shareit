package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestResponseDto create(ItemRequest request, long userId);

    Collection<ItemRequestResponseDto> getByUser(long userId);

    ItemRequestResponseDto getByIdWithItems(long requestId, long userId);

    ItemRequest getById(long requestId, long userId);

    Collection<ItemRequestResponseDto> getAll(long userId, int from, int size);
}
