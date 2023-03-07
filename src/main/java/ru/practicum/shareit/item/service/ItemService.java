package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Item item, long userId);

    Item update(ItemDto item, long itemId, long userId);

    Item getItemById(long id);

    Item getItemByUser(long userId, long itemId);

    Item getItemNotOwnedByUser(long userId, long itemId);

    List<Item> getItemsByUser(long userId);

    void deleteItemById(long itemId, long userId);

    List<ItemResponseDto> getByText(long userId, String text);

    Comment addComment(long userId, long itemId, CommentRequestDto dto);
}
