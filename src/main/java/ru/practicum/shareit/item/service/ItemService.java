package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(ItemDto item, long userId);

    Item update(ItemDto item, long itemId, long userId);

    Item getItemById(long id, long userId);

    Item getItemByUser(long userId, long itemId);

    Item getItemNotOwnedByUser(long userId, long itemId);

    List<Item> getItemsForOwner(long userId, int from, int size);

    Item getItemForOwner(long userId, long itemId);

    void deleteItemById(long itemId, long userId);

    List<Item> getByText(long userId, String text, int from, int size);

    Comment addComment(long userId, long itemId, CommentRequestDto dto);
}
