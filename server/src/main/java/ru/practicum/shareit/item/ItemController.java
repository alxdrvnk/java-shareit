package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j(topic = "ItemController: ")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ItemResponseDto create(@RequestBody ItemDto itemDto,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("Create Item request. Data: %s", itemDto));
        return itemMapper.toItemDto(
                itemService.create(itemDto, userId));
    }

    @PatchMapping("/{id}")
    public ItemResponseDto update(@PathVariable("id") long id,
                                  @RequestBody ItemDto itemDto,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("Update Item with id: %d . Data: %s", id, itemDto));
        return itemMapper.toItemDto(
                itemService.update(itemDto, id, userId));
    }

    @GetMapping("/{id}")
    public ItemResponseDto getItemById(@PathVariable("id") long id,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("Get Item by id: %d", id));
        return itemMapper.toItemDto(itemService.getItemById(id, userId));
    }

    @GetMapping
    public List<ItemResponseDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "20") int size) {
        log.info(String.format("Get Item by User Id: %d. Params: from=%d, sise=%d", userId, from, size));
        return itemMapper.toItemDtoList(itemService.getItemsForOwner(userId, from, size));
    }

    @GetMapping("/search")
    public List<ItemResponseDto> findByNameAndDescription(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "20") int size,
                                                          @RequestParam String text) {
        log.info(String.format("Search Item request with text: \"%s\"", text));
        return itemMapper.toItemDtoList(itemService.getByText(userId, text, from, size));
    }

    @PostMapping("/{id}/comment")
    public CommentResponseDto addComment(@RequestBody CommentRequestDto commentDto,
                                         @PathVariable("id") long itemId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(
                String.format(
                        "Add COMMENT request from User with Id: %d for Item with Id: %d",
                        userId,
                        itemId));
        return commentMapper.toCommentResponseDto(itemService.addComment(userId, itemId, commentDto));
    }
}
