package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao storage;
    private final UserService userService;

    @Override
    public Item create(Item item, long userId) {
        User user = userService.getUserBy(userId);
        return storage.create(item.withOwner(user));
    }

    @Override
    public Item update(ItemDto itemDto, long itemId, long userId) {
        Item actualItem = getItemByUser(userId, itemId);
        Item item = ItemMapper.patchItem(itemDto, actualItem);
        if (storage.update(item) == 0) {
            throw new ShareItNotFoundException(
                    String.format("Item with id: %s not found", item.getId()));
        }
        return item;
    }

    @Override
    public Item getItemById(long id) {
        return storage.get(id).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Item with id: %s not found", id)));
    }

    @Override
    public Item getItemByUser(long userId, long itemId) {
        return storage.getItemByUser(userId, itemId).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Item with id: %s does not belong to User with id: %s", itemId, userId)));
    }

    @Override
    public List<Item> getItemsByUser(long userId) {
        return storage.getUserItems(userId);
    }

    @Override
    public List<Item> getAvailableItems(String text) {
        return storage.getAvailableItems(text.toLowerCase());
    }

    @Override
    public void deleteItemById(long itemId, long userId) {
        if (storage.delete(itemId, userId) == 0) {
            throw new ShareItNotFoundException(
                    String.format("Can't delete Item Id: %s and User Id: %s", itemId, userId));
        }
    }
}
