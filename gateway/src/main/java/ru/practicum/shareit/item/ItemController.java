package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentGatewayDto;
import ru.practicum.shareit.item.dto.ItemGatewayDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Controller
@Validated
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "20") @Positive int size) {
        log.info(String.format("Get Items by User with id: %d and params: from=%d, size=%d", userId, from, size));
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable(name = "id") long itemId) {
        log.info(String.format("Get Item with id: %d by User with id: %d", itemId, userId));
        return itemClient.getItem(userId, itemId);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "20") @Positive int size) {
        log.info(String.format("Search Items by text: '%s'. User with id: %d. Params: from=%d, size=%d", text, userId, from, size));
        return itemClient.searchItem(userId, text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Valid @RequestBody ItemGatewayDto dto) {
        log.info(String.format("Create Item with data:%s, by User with id: %d", dto, userId));
        return itemClient.addItem(userId, dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable(name = "id") long itemId,
                                             @RequestBody ItemGatewayDto dto) {
        log.info(String.format("Update Item with id:%d by User with id: %d. Data:%s", itemId, userId, dto));
        return itemClient.updateItem(userId, itemId, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name = "id") long itemId) {
        log.info(String.format("Delete Item with id: %d", itemId));
        itemClient.deleteItem(userId, itemId);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable(name = "id") long itemId,
                                             @Validated @RequestBody CommentGatewayDto dto) {
        log.info(String.format("Add comment with data: %s for Item with id: %d", dto, itemId));
        return itemClient.addComment(userId, itemId, dto);
    }
}
