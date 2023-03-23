package ru.practicum.shareit.request.repository

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import ru.practicum.shareit.configuration.DbUnitConfig
import ru.practicum.shareit.request.model.ItemRequest
import ru.practicum.shareit.user.model.User
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
@ContextConfiguration(classes = DbUnitConfig.class)
@TestExecutionListeners([TransactionDbUnitTestExecutionListener, DependencyInjectionTestExecutionListener])
class ItemRequestRepositorySpec extends Specification {

    @Autowired
    private ItemRequestRepository repository

    @DatabaseSetup(value = "classpath:database/set_user.xml", connection = "dbUnitDatabaseConnection")
    def "Should create new ItemRequest with generated id"() {
        given:
        def itemRequest = ItemRequest.builder()
                .description("Test item request description")
                .requester(User.builder().id(1L).build())
                .created(LocalDateTime.of(2007, 9, 1, 12, 0, 0))
                .build()


        when:
        def id = repository.save(itemRequest).getId()

        then:
        id == 1
    }

    @DatabaseSetup(value = "classpath:database/set_item_request.xml", connection = "dbUnitDatabaseConnection")
    def "Should return ItemRequestResponseDto with items list"() {
        when:
        def itemRequest = repository.getByRequesterId(1L)

        then:
        itemRequest.size() == 1

        with(itemRequest) {
            id == [1]
            description == ["Test request description"]
            created == [LocalDateTime.of(2007, 9, 1, 12, 0, 0)]
            items.id == [[1, 2]]
            items.name == [["Request item 1", "Request item 2"]]
            items.ownerId == [[2, 3]]
        }
    }

    @DatabaseSetup(value = "classpath:database/set_item_request.xml", connection = "dbUnitDatabaseConnection")
    def "Should return ItemRequestResponseDto when get by id"() {
        when:
        def itemRequest = repository.getItemRequestById(1L).get()

        then:
        itemRequest.getId() == 1L
        itemRequest.getDescription() == "Test request description"
        itemRequest.getCreated() == LocalDateTime.of(2007, 9, 1, 12, 0, 0)
        with(itemRequest.getItems()) {
            id == [1, 2]
            name == ["Request item 1", "Request item 2"]
            ownerId == [2, 3]
        }
    }

    def "Should return empty when get by unexpected ItemRequest id"() {
        when:
        def itemRequest = repository.getItemRequestById(1L)

        then:
        itemRequest.isEmpty()
    }
}
