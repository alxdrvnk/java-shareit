package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        User user  = userService.getUserBy(userId);
        return ItemMapper.toItemDto(
                itemService.create(ItemMapper.toItem(itemDto).withOwner(user)));
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") long id,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return null;
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") long id) {
        return null;
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return null;
    }

    @GetMapping("/search")
    public List<ItemDto> getAvailableItems(@RequestHeader("H-Sharer-User-Id") long userId,
                                           @RequestParam(name = "text") String text) {
        return null;
    }
}
