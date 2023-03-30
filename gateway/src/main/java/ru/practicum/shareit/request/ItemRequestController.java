package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestGatewayDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j(topic = "Gateway ItemRequestController")
@Validated
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("Get requests by User with id: %d", userId));
        return itemRequestClient.getRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "20") @Positive int size) {
        log.info(String.format("Get all requests by User: %d with params: from=%d, size=%d", userId, from, size));
        return itemRequestClient.getRequests(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable(name = "id") long requestId) {
        log.info(String.format("Get request with id: %d by User with id: %d", requestId, userId));
        return itemRequestClient.getRequest(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Validated ItemRequestGatewayDto dto) {
        log.info(String.format("Create request from User with id: %d. Data: %s", userId, dto));
        return itemRequestClient.addRequest(userId, dto);
    }
}
