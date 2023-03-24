package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ShareItBadRequest;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j(topic = "ItemRequestService")
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final Clock clock;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    public ItemRequestResponseDto create(ItemRequest request, long userId) {
        userService.getUserBy(userId); // Требуется возвращать 404 если пользователь не найден
        return itemRequestMapper.toItemRequestResponseDto(
                itemRequestRepository.save(request.withCreated(LocalDateTime.now(clock))));
    }

    @Override
    public Collection<ItemRequestResponseDto> getByUser(long userId) {
        userService.getUserBy(userId); // Требуется возвращать 404 если пользователь не найден
        return itemRequestRepository.getByRequesterId(userId);
    }

    @Override
    public ItemRequestResponseDto getById(long requestId) {
        return itemRequestRepository.getItemRequestById(requestId).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("ItemRequest with id: %d not found", requestId)));
    }

    @Override
    public Collection<ItemRequestResponseDto> getAll(long userId, int from, int size) {
        if (from >= 0 && size > 0) {
            return itemRequestRepository.findByRequesterIdNot(userId, from, size);
        } else {
            throw new ShareItBadRequest(
                    String.format("Pagination parameters are not valid: from=%d, size=%d", from, size));
        }
    }
}
