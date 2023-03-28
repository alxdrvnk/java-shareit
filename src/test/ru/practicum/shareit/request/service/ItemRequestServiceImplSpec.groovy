package ru.practicum.shareit.request.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import ru.practicum.shareit.cofiguration.ClockConfig
import ru.practicum.shareit.exceptions.ShareItBadRequest
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.request.mapper.ItemRequestMapper
import ru.practicum.shareit.request.model.ItemRequest
import ru.practicum.shareit.request.repository.ItemRequestRepository
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.service.UserService
import spock.lang.Specification

import java.time.Clock
import java.time.LocalDateTime

@ContextConfiguration(classes = ClockConfig.class)
class ItemRequestServiceImplSpec extends Specification {

    private final ItemRequestMapper mapper = new ItemRequestMapper()

    @Autowired
    private Clock clock

    def "Should return 404 if user not exists when create ItemRequest"() {
        given:
        def itemRequest = ItemRequest.builder().build()

        and:
        def repository = Stub(ItemRequestRepository)
        def userService = Stub(UserService) {
            getUserBy(1L) >> {
                throw new ShareItNotFoundException("")
            }
        }
        def service = new ItemRequestServiceImpl(clock, repository, userService)

        when:
        service.create(itemRequest, 1L)

        then:
        thrown(ShareItNotFoundException.class)
    }

    def "Should return ItemRequestResponseDto when create ItemRequest"() {
        given:
        def itemRequest = ItemRequest.builder()
                .description("Test itemRequest")
                .build()

        and:
        def repository = Stub(ItemRequestRepository) {
            save(_ as ItemRequest) >> {
                return ItemRequest.builder()
                        .id(1L)
                        .description("Test itemRequest")
                        .created(LocalDateTime.of(2007, 9, 1, 12, 0, 0))
                        .requester(User.builder().build())
                        .build()
            }
        }
        def userService = Stub(UserService) {
            getUserBy(1L) >> {
                return User.builder().build()
            }
        }
        def service = new ItemRequestServiceImpl(clock, repository, userService)

        when:
        def itemRequestResponseDto = service.create(itemRequest, 1L)

        then:
        itemRequestResponseDto.id == 1
        itemRequestResponseDto.description == "Test itemRequest"
        itemRequestResponseDto.created == LocalDateTime.of(2007, 9, 1, 12, 0, 0)
    }

    def "Should return 404 if user not exists when get ItemRequest by requester id"() {
        given:
        def repository = Stub(ItemRequestRepository)
        def userService = Stub(UserService) {
            getUserBy(1L) >> {
                throw new ShareItNotFoundException("")
            }
        }
        def service = new ItemRequestServiceImpl(clock, repository, userService)

        when:
        service.getByUser(1L)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should return 404 when get non exists ItemRequests by id"() {
        given:
        def repository = Stub(ItemRequestRepository)
        def userService = Stub(UserService)
        def service = new ItemRequestServiceImpl(clock, repository, userService)

        when:
        service.getById(1L, 1L)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should return 400 when pagination params is page=0 and size=0"() {
        given:
        def repository = Stub(ItemRequestRepository)
        def userService = Stub(UserService)
        def service = new ItemRequestServiceImpl(clock, repository, userService)

        when:
        service.getAll(1L, 0, 0)
        then:
        thrown(ShareItBadRequest)
    }

    def "Should return 400 when pagination params is page=-1 and size=3"() {
        given:
        def repository = Stub(ItemRequestRepository)
        def userService = Stub(UserService)
        def service = new ItemRequestServiceImpl(clock, repository, userService)

        when:
        service.getAll(1L, -1, 3)
        then:
        thrown(ShareItBadRequest)
    }

    def "Should return 400 when pagination params is page=0 and size=-1"() {
        given:
        def repository = Stub(ItemRequestRepository)
        def userService = Stub(UserService)
        def service = new ItemRequestServiceImpl(clock, repository, userService)

        when:
        service.getAll(1L, 0, -1)
        then:
        thrown(ShareItBadRequest)
    }
}
