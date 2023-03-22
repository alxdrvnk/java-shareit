package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class, BookingMapper.class}, injectionStrategy = InjectionStrategy.FIELD)
public interface ItemMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item updateItemFromDto(ItemDto itemDto, @MappingTarget Item.ItemBuilder item);

    Item toItem(ItemDto itemDto);

    Item toItem(ItemResponseDto itemResponseDto);

    ItemResponseDto toItemDto(Item item);

    List<ItemResponseDto> toItemDtoList(List<Item> items);
}
