package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper MAPPER = Mappers.getMapper(ItemMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item updateItemFromDto(ItemDto itemDto, @MappingTarget Item.ItemBuilder item);

    Item toItem(ItemDto itemDto);

    ItemResponseDto toItemDto(Item item);

    List<ItemResponseDto> toItemDtoList(List<Item> items);
}
