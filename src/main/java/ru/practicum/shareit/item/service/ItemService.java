package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Item item);

    Item update(Item item);

    List<Item> getAllItems();

    Item getItemById(long id);

    List<Item> getItemsByUser(long userId);

    List<Item> getAvailableItems();

    void deleteItemById(long itemId);
}
