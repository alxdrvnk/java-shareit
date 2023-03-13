package ru.practicum.shareit.item.mapper


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
                .owner(User.builder().build())
                .build()
        when:
        def dto = mapper.toItemDto(item)
        then:
        dto.id == 1
    }

    
}