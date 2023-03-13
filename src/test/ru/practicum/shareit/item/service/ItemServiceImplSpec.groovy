package ru.practicum.shareit.item.service

import ru.practicum.shareit.booking.repository.BookingRepository
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.item.dto.ItemDto
import ru.practicum.shareit.item.mapper.CommentMapper
import ru.practicum.shareit.item.mapper.CommentMapperImpl
import ru.practicum.shareit.item.mapper.ItemMapper
import ru.practicum.shareit.item.mapper.ItemMapperImpl
import ru.practicum.shareit.item.repository.CommentRepository
import ru.practicum.shareit.item.repository.ItemRepository
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.service.UserService
import spock.lang.Specification

class ItemServiceImplSpec extends Specification {

    private final ItemMapper itemMapper = new ItemMapperImpl()
    private final CommentMapper commentMapper = new CommentMapperImpl()

    def "Should throw ShareItNotFoundException if update item not-exists"() {
        given:
        def item = ItemDto.builder()
                .name("Non-exists item")
                .description("non-exists")
                .available(true)
                .owner(User.builder().build())
                .request(null)
                .build()

        def itemRepository = Stub(ItemRepository) {
            findByIdAndOwnerId(_ as long, _ as long) >> Optional.empty()
        }
        def userService = Mock(UserService)
        def bookingRepository = Mock(BookingRepository)
        def commentRepository = Mock(CommentRepository)


        def service = new ItemServiceImpl(
                itemMapper,
                commentMapper,
                itemRepository,
                userService,
                bookingRepository,
                commentRepository)

        when:
        service.update(item, 9999, 9999)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should throw ShareItNotFoundException when get non-exists item"() {
        given:
        def itemRepository = Stub(ItemRepository) {
            findByIdAndOwnerId(_ as long, _ as long) >> Optional.empty()
        }
        def userService = Mock(UserService)
        def bookingRepository = Mock(BookingRepository)
        def commentRepository = Mock(CommentRepository)


        def service = new ItemServiceImpl(
                itemMapper,
                commentMapper,
                itemRepository,
                userService,
                bookingRepository,
                commentRepository)

        when:
        service.getItemById(9999)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should throw ShareItNotFoundException when get item does not belong to user"() {
        given:
        def itemRepository = Stub(ItemRepository) {
            findByIdAndOwnerId(_ as long, _ as long) >> Optional.empty()
        }
        def userService = Mock(UserService)
        def bookingRepository = Mock(BookingRepository)
        def commentRepository = Mock(CommentRepository)


        def service = new ItemServiceImpl(
                itemMapper,
                commentMapper,
                itemRepository,
                userService,
                bookingRepository,
                commentRepository)

        when:
        service.getItemByUser(9999, 9999)

        then:
        thrown(ShareItNotFoundException)
    }
}
