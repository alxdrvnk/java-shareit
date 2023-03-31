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
        return mapper.toItemRequestResponseDto(itemRequestService.create(mapper.toItemRequest(dto), userId));
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getById(@PathVariable("requestId") long requestId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return mapper.toItemRequestResponseDto(itemRequestService.getById(requestId, userId));
    }

    @GetMapping
    public Collection<ItemRequestResponseDto> getByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return mapper.toItemRequestResponseDtoList(itemRequestService.getByUser(userId));
    }

    @GetMapping("/all")
    public Collection<ItemRequestResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "20") int size) {
        return mapper.toItemRequestResponseDtoList(itemRequestService.getAll(userId, from, size));
    }

//    private void validatePaginationParams(int from, int size) {
//        if (from < 0 || size < 1) {
//            throw new ShareItBadRequest(String.format("Wrong parameters: from = %d and size = %d", from, size));
//        }
//    }
}
