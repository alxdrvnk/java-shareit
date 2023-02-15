package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemDao {

    private final HashMap<Long, Set<Long>> userItemsMap = new HashMap<>();
    private final HashMap<Long, Item> itemsMap = new HashMap<>();

    private long id = 1L;

    private long getNextId() {
        return id++;
    }

    @Override
    public Item create(Item item) {
        long itemId = getNextId();
        itemsMap.put(itemId, item.withId(itemId));
        userItemsMap.compute(item.getOwner().getId(), (userId, itemsList) -> {
            if (itemsList == null) {
                itemsList = new HashSet<>();
            }
            itemsList.add(itemId);
            return itemsList;
        });
        return item.withId(itemId);
    }

    @Override
    public int update(Item item) {

        if (itemsMap.containsKey(item.getId())) {
            itemsMap.put(item.getId(), item);
            return 1;
        }
        return 0;

    }

    @Override
    public Optional<Item> get(long id) {
        try {
            return Optional.of(itemsMap.get(id));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> getUserItems(long userId) {
        List<Item> items = new ArrayList<>();

        for (long id : userItemsMap.get(userId)) {
            items.add(itemsMap.get(id));
        }
        return items;
    }

    @Override
    public List<Item> getAvailableItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemsMap.values().stream()
                .filter(item -> item.isAvailable() && isContainsText(item, text))
                .collect(Collectors.toList());
    }

    @Override
    public int delete(long itemId, long userId) {
        if (userItemsMap.containsKey(userId) && userItemsMap.get(itemId).contains(itemId)) {
            itemsMap.remove(itemId);
            userItemsMap.get(userId).remove(itemId);
            return 1;
        } else {
            return 0;
        }
    }

    private boolean isContainsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text) ||
                item.getDescription().toLowerCase().contains(text);
    }
}
