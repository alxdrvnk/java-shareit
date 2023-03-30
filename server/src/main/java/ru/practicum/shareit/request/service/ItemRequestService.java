package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequest create(ItemRequest request, long userId);

    Collection<ItemRequest> getByUser(long userId);

    ItemRequest getById(long requestId, long userId);

    Collection<ItemRequest> getAll(long userId, int from, int size);
}
