package ru.practicum.shareit.item.mapper

import ru.practicum.shareit.item.dto.ItemDto
import ru.practicum.shareit.item.model.Item
import ru.practicum.shareit.user.model.User
import spock.lang.Specification

class ItemMapperSpec extends Specification {

    ItemMapper mapper = new ItemMapperImpl()

    def "Should map Item to ItemResponseDto"() {
        given:
        User user = User.builder()
                .id(1L)
                .name("User")
                .email("mail@mail.mail")
                .build()

        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Item")
                .available(true)
                .owner(user)
                .build()
        when:
        def dto = mapper.toItemDto(item)
        then:
        dto.id == 1
        dto.name == "Item"
        dto.description == "Item"
        dto.available == true
        dto.owner == user
        dto.comments == null
    }

    def "Should partial map Item from ItemDto"() {
        given:
        User user = User.builder()
                .id(1L)
                .name("User")
                .email("mail@mail.mail")
                .build()

        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Item")
                .available(true)
                .owner(user)
                .build()

        ItemDto itemDto = ItemDto.builder()
                .name("NewName")
                .build()
        when:
        def mappedItem = mapper.updateItemFromDto(itemDto, item.toBuilder())
        then:
        mappedItem.id == 1
        mappedItem.name == "NewName"
        mappedItem.description == "Item"
        mappedItem.available
        mappedItem.owner == user
        mappedItem.comments == null
    }
}
