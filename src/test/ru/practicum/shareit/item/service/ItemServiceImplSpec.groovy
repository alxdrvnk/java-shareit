package ru.practicum.shareit.item.service

import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.item.dto.ItemDto
import ru.practicum.shareit.item.model.Item
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.service.UserService
import spock.lang.Specification

class ItemServiceImplSpec extends Specification {

    def "Should throw ShareItNotFoundException if update item not-exists"() {
        given:
        def item = ItemDto.builder()
                .name("Non-exists item")
                .description("non-exists")
                .available(true)
                .owner(User.builder().build())
                .request(null)
                .build()

        def dao = Stub(ItemDao) {
           update(_ as Item) >> 0
        }

        def service = new ItemServiceImpl(dao, Stub(UserService))

        when:
        service.update(item, 9999, 9999)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should throw ShareItNotFoundException when get non-exists item" () {
        given:
        def dao = Stub(ItemDao) {
            get(_ as long) >> Optional.empty()
        }

        def service = new ItemServiceImpl(dao, Stub(UserService))

        when:
        service.getItemById(9999)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should throw ShareItNotFoundException when get item does not belong to user" () {
        given:
        def dao = Stub(ItemDao) {
            getItemByUser(_ as long, _ as long) >> Optional.empty()
        }

        def service = new ItemServiceImpl(dao, Stub(UserService))

        when:
        service.getItemByUser(9999, 9999)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should throw ShareItNotFoundException when delete non-exists item" () {
        given:
        def dao = Stub(ItemDao) {
            delete(_ as Long, _ as Long) >> 0
        }

        def service = new ItemServiceImpl(dao, Stub(UserService))

        when:
        service.deleteItemById(9999L, 9999L)

        then:
        thrown(ShareItNotFoundException)
    }
}
