package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Item item, long userId);

    Item update(ItemDto item, long itemId, long userId);

    Item getItemById(long id);

    Item getItemByUser(long userId, long itemId);

    List<Item> getItemsByUser(long userId);

    List<Item> getAvailableItems(String text);

    void deleteItemById(long itemId, long userId);
}
