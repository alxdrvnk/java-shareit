package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {

    Item create(Item item);

    int update(Item item);

    Optional<Item> get(long id);

    Optional<Item> getItemByUser(long userId, long itemId);

    List<Item> getUserItems(long userId);

    List<Item> getAvailableItems(String text);

    int delete(long itemId, long userId);
}
