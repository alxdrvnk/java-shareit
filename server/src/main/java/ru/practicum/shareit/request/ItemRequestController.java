package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@Slf4j(topic = "ItemRequestController")
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestMapper mapper;

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto create(@RequestBody ItemRequestCreationDto dto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("Create Item Request by User with Id:%d . Data: %s", userId, dto));
        return mapper.toItemRequestResponseDto(itemRequestService.create(mapper.toItemRequest(dto), userId));
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getById(@PathVariable("requestId") long requestId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("Get Item Request with Id: %d and  User id: %d", requestId, userId));
        return mapper.toItemRequestResponseDto(itemRequestService.getById(requestId, userId));
    }

    @GetMapping
    public Collection<ItemRequestResponseDto> getByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(String.format("Get all Item Requests by User id: %d", userId));
        return mapper.toItemRequestResponseDtoList(itemRequestService.getByUser(userId));
    }

    @GetMapping("/all")
    public Collection<ItemRequestResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "20") int size) {
        log.info("Get all Item Requests");
        return mapper.toItemRequestResponseDtoList(itemRequestService.getAll(userId, from, size));
    }
}
