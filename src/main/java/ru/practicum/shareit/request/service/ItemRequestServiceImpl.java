package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j(topic = "ItemRequestService")
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private Clock clock;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestResponseDto create(long userId, ItemRequest request) {
        return itemRequestMapper.toItemRequestResponseDto(
                itemRequestRepository.save(request.withCreated(LocalDateTime.now(clock))));
    }

    @Override
    public Collection<ItemRequestResponseDto> getByUser(long userId) {
        return itemRequestRepository.getByRequesterId(userId);
    }

    @Override
    public ItemRequestResponseDto getById(long requestId) {
        return itemRequestRepository.getItemRequestById(requestId).orElseThrow();
    }

    @Override
    public Collection<ItemRequestResponseDto> getAll(long userId, long from, int size) {
        return null;
    }
}
