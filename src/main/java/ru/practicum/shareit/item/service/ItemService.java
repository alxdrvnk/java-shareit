package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Item item);

    Item update(Item item, Long userId);

    Item getItemById(long id);

    List<Item> getItemsByUser(long userId);

    List<Item> getAvailableItems(String text);

    void deleteItemById(long itemId, long userId);
}
