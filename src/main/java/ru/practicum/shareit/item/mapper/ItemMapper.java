package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final CommentMapper commentMapper;

    public ItemResponseDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }

        Long requestId = null;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }

        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(item.getOwner())
                .requestId(requestId)
                .comments(
                        commentMapper.toCommentDtoList(item.getComments()))
                .build();
    }

    public Item updateItemFromDto(ItemDto itemDto, Item.ItemBuilder item, ItemRequest request) {
        if (itemDto == null) {
            return item.build();
        }

        if (itemDto.getName() != null) {
            item.name(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.description(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.available(itemDto.getAvailable());
        }

        if (request != null) {
            item.request(request);
        }

        return item.build();
    }

    public List<ItemResponseDto> toItemDtoList(List<Item> items) {
        //Есть вопрос как реализовать лучше.
        // Если делать через stream, то нужно делать toItemDto метод static - это смотрится как то странно
        // если мне нужен просто метод toItemDto, то я могу не создавать объект ItemMapper.
        // Если нужно использовать все остальные методы, то объект необходимо создать.
        //return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());

        if (items == null) {
            return Collections.emptyList();
        }

        List<ItemResponseDto> list = new ArrayList<>(items.size());
        for (Item item : items) {
            list.add(toItemDto(item));
        }
        return list;
    }

    public Item toItem(ItemDto itemDto) {
        Item.ItemBuilder builder = Item.builder();

        if (itemDto.getId() != null) {
            builder.id(itemDto.getId());
        }
        if (itemDto.getAvailable() != null) {
            builder.available(itemDto.getAvailable());
        }

        return builder
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .build();
    }

    public Item toItem(ItemResponseDto itemResponseDto) {
        return Item.builder()
                .id(itemResponseDto.getId())
                .name(itemResponseDto.getName())
                .description(itemResponseDto.getDescription())
                .available(itemResponseDto.getAvailable())
                .owner(itemResponseDto.getOwner())
                .build();
    }
}
