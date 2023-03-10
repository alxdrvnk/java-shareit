package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("ItemController: create Item request. Data: %s", itemDto));
        return ItemMapper.toItemDto(
                itemService.create(ItemMapper.toItem(itemDto), userId));
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") long id,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("ItemController: update Item with id: %d . Data: %s", id, itemDto));
        return ItemMapper.toItemDto(
                itemService.update(itemDto, id, userId));
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") long id) {
        return ItemMapper.toItemDto(itemService.getItemById(id));
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> getAvailableItems(@RequestParam(name = "text") String text) {
        return itemService.getAvailableItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
