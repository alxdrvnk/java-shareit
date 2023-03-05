package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ShareItBadRequest;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Transactional
    @Override
    public Item create(Item item, long userId) {
        User user = userService.getUserBy(userId);
        return itemRepository.save(item.withOwner(user));
    }

    @Transactional
    @Override
    public Item update(ItemDto itemDto, long itemId, long userId) {
        Item actualItem = getItemByUser(userId, itemId);
        Item item = ItemMapper.MAPPER.updateItemFromDto(itemDto, actualItem.toBuilder());
        return itemRepository.save(item);
    }

    @Override
    public Item getItemById(long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Item with id: %s not found", id)));
    }

    @Override
    public Item getItemByUser(long userId, long itemId) {
        return itemRepository.findByIdAndOwnerId(itemId, userId).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Item with id: %s does not belong to User with id: %s", itemId, userId)));
    }

    @Override
    public Item getItemNotOwnedByUser(long userId, long itemId) {
        return itemRepository.findByIdAndOwnerIdNot(itemId, userId).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Item with id: %d not found", itemId)));
    }

    @Override
    public List<Item> getItemsByUser(long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Transactional
    @Override
    public void deleteItemById(long itemId, long userId) {
        itemRepository.deleteByIdAndOwnerId(itemId, userId);
    }

    @Override
    public List<ItemDto> getByText(long userId, String text) {
        userService.getUserBy(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return ItemMapper.MAPPER.toItemDtoList(itemRepository.searchByText(text));
    }
}
