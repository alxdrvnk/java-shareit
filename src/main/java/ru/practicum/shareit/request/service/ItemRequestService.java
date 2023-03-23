package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestResponseDto create(long userId, ItemRequest request);

    Collection<ItemRequestResponseDto> getByUser(long userId);

    ItemRequestResponseDto getById(long requestId);

    Collection<ItemRequestResponseDto> getAll(long userId, long from, int size);
}
