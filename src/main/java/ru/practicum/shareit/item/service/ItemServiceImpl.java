package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final UserService userService;
    private final InMemoryItemStorage storage;

    @Override
    public Item create(Item item) {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public List<Item> getAllItems() {
        return null;
    }

    @Override
    public Item getItemById(long id) {
        return null;
    }

    @Override
    public List<Item> getItemsByUser(long userId) {
        return null;
    }

    @Override
    public List<Item> getAvailableItems() {
        return null;
    }

    @Override
    public void deleteItemById(long itemId) {
    }
}
