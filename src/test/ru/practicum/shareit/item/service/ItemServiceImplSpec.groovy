package ru.practicum.shareit.item.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import ru.practicum.shareit.booking.repository.BookingRepository
import ru.practicum.shareit.cofiguration.ClockConfig
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.item.dto.ItemDto
import ru.practicum.shareit.item.mapper.CommentMapper
import ru.practicum.shareit.item.mapper.ItemMapper
import ru.practicum.shareit.item.repository.CommentRepository
import ru.practicum.shareit.item.repository.ItemRepository
import ru.practicum.shareit.request.service.ItemRequestService
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.service.UserService
import spock.lang.Specification

import java.time.Clock

@ContextConfiguration(classes = ClockConfig.class)
class ItemServiceImplSpec extends Specification {

    private final ItemMapper itemMapper = new ItemMapper()
    private final CommentMapper commentMapper = new CommentMapper()

    @Autowired
    private Clock clock

    def "Should throw ShareItNotFoundException if update item not-exists"() {
        given:
        def item = ItemDto.builder()
                .name("Non-exists item")
                .description("non-exists")
                .available(true)
                .owner(User.builder().build())
                .build()

        def itemRepository = Stub(ItemRepository) {
            findByIdAndOwnerId(_ as long, _ as long) >> Optional.empty()
        }
        def userService = Mock(UserService)
        def bookingRepository = Mock(BookingRepository)
        def commentRepository = Mock(CommentRepository)
        def itemRequestService = Mock(ItemRequestService)


        def service = new ItemServiceImpl(
                clock,
                itemMapper,
                commentMapper,
                itemRepository,
                userService,
                bookingRepository,
                commentRepository,
                itemRequestService)

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
        def itemRequestService = Mock(ItemRequestService)

        def service = new ItemServiceImpl(
                clock,
                itemMapper,
                commentMapper,
                itemRepository,
                userService,
                bookingRepository,
                commentRepository,
                itemRequestService)

        when:
        service.getItemById(9999, 0)

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
        def itemRequestService = Mock(ItemRequestService)

        def service = new ItemServiceImpl(
                clock,
                itemMapper,
                commentMapper,
                itemRepository,
                userService,
                bookingRepository,
                commentRepository,
                itemRequestService)

        when:
        service.getItemByUser(9999, 9999)

        then:
        thrown(ShareItNotFoundException)
    }
}
