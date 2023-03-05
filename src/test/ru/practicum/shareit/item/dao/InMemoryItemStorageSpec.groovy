package ru.practicum.shareit.item.dao

import ru.practicum.shareit.item.model.Item
import ru.practicum.shareit.user.model.User
import spock.lang.Specification

class InMemoryItemStorageSpec extends Specification {

    HashMap<Long, Set<Long>> userItemMap
    HashMap<Long, Item> itemsMap
    InMemoryItemStorage inMemory

    def setup () {
        userItemMap = new HashMap()
        itemsMap = new HashMap()
        inMemory = new InMemoryItemStorage(userItemMap, itemsMap)
    }

    def "Should return item with id when create item" () {
        given:
        def item = Item.builder()
        .name("item")
        .description("item 1")
        .available(true)
        .owner(
                User.builder()
                .id(1)
                .name("User1")
                .email("user@email.com")
                .build()
        )
        .request(null)
        .build()

        when:
        inMemory.create(item)

        then:
        userItemMap.size() == 1
        itemsMap.size() == 1

        itemsMap.get(1L) == item.withId(1)
        userItemMap.get(1L).contains(1L)
    }

    def "Should return 0 when try to update non-exists item" () {

        given:
        def item = Item.builder()
                .name("item")
                .description("item 1")
                .available(true)
                .owner(
                        User.builder()
                                .id(1)
                                .name("User1")
                                .email("user@email.com")
                                .build()
                )
                .request(null)
                .build()

        expect:
        inMemory.update(item) == 0
    }

    def "Should return empty when get non-exists item" () {
        expect:
        inMemory.get(1L) == Optional.empty()
    }

    def "Should return list user items" () {
        given:
        def item = Item.builder()
                .name("item")
                .description("item 1")
                .available(true)
                .owner(
                        User.builder()
                                .id(1)
                                .name("User1")
                                .email("user@email.com")
                                .build()
                )
                .request(null)
                .build()

        and:
        inMemory.create(item)

        when:
        def items = inMemory.getUserItems(1)

        then:
        items.size() == 1
        items.get(0) == item.withId(1)
    }

    def "Should return list of items when search items by text" () {
        given:
        def item1 = Item.builder()
                .name("item1")
                .description("item 1")
                .available(false)
                .owner(
                        User.builder()
                                .id(1)
                                .name("User1")
                                .email("user@email.com")
                                .build()
                )
                .request(null)
                .build()

        def item2 = Item.builder()
                .name("item2")
                .description("item2")
                .available(true)
                .owner(
                        User.builder()
                                .id(1)
                                .name("User1")
                                .email("user@email.com")
                                .build()
                )
                .request(null)
                .build()

        and:
        inMemory.create(item1)
        inMemory.create(item2)

        when:
        def items = inMemory.getAvailableItems("item")

        then:
        items.size() == 1
        items.get(0) == item2.withId(2)
    }
}
