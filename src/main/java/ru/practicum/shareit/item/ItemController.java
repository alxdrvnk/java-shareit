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

import javax.persistence.PreUpdate;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ItemResponseDto create(@Valid @RequestBody ItemDto itemDto,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("ItemController: create Item request. Data: %s", itemDto));
        return itemMapper.toItemDto(
                itemService.create(itemMapper.toItem(itemDto), userId));
    }

    @PatchMapping("/{id}")
    public ItemResponseDto update(@PathVariable("id") long id,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("ItemController: update Item with id: %d . Data: %s", id, itemDto));
        return itemMapper.toItemDto(
                itemService.update(itemDto, id, userId));
    }

    @GetMapping("/{id}")
    public ItemResponseDto getItemById(@PathVariable("id") long id) {
        return itemMapper.toItemDto(itemService.getItemById(id));
    }

    @GetMapping
    public List<ItemResponseDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsByUser(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemResponseDto> findByNameAndDescription(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam String text) {
        log.info(String.format("ItemController: search Item request wiht text: \"%s\"", text));
        return itemService.getByText(userId, text);
    }

    @PostMapping("/{id}/comment")
    public CommentResponseDto addComment(@Valid @RequestBody CommentRequestDto commentDto,
                                         @PathVariable("id") long itemId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(
                String.format(
                        "ItemController: add COMMENT request from User with Id: %d for Item with Id: %d",
                        userId,
                        itemId));
        return commentMapper.toCommentResponseDto(itemService.addComment(userId, itemId, commentDto));
    }
}
