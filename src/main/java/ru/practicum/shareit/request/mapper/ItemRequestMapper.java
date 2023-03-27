package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {

    private final ItemMapper itemMapper;

    public ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemMapper.toItemForItemRequestDtoList(itemRequest.getItems()))
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestCreationDto dto) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .build();
    }

    public Collection<ItemRequestResponseDto> toItemRequestResponseDtoList(Collection<ItemRequest> requests) {
        return requests.stream().map(this::toItemRequestResponseDto).collect(Collectors.toList());
    }
}
