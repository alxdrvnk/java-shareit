package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

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
                .lastBooking(bookingMapper.toBookingItemDto(item.getLastBooking()))
                .nextBooking(bookingMapper.toBookingItemDto(item.getNextBooking()))
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
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
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

    public ItemForItemRequestDto toItemForItemRequestDto(Item item) {
        return ItemForItemRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .requestId(item.getRequest().getId())
                .ownerId(item.getOwner().getId())
                .build();
    }

    public Collection<ItemForItemRequestDto> toItemForItemRequestDtoList(Collection<Item> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream().map(this::toItemForItemRequestDto).collect(Collectors.toList());
    }
}
