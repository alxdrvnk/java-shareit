package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();

    }

    public static Item toItem(ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }

        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .request(itemDto.getRequest())
                .build();
    }

    public static Item patchItem(ItemDto from, Item to) {
        var item = Item.builder()
                .id(to.getId())
                .name(to.getName())
                .description(to.getDescription())
                .available(to.isAvailable())
                .owner(to.getOwner())
                .request(to.getRequest());


        if (from.getName() != null) {
            item.name(from.getName());
        }

        if (from.getAvailable() != null) {
            item.available(from.getAvailable());
        }

        if (from.getDescription() != null) {
            item.description(from.getDescription());
        }

        return item.build();
    }
}
