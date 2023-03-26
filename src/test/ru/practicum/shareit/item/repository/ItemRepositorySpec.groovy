package ru.practicum.shareit.item.repository

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import ru.practicum.shareit.configuration.DbUnitConfig
import ru.practicum.shareit.item.model.Item
import ru.practicum.shareit.user.model.User
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
@ContextConfiguration(classes = DbUnitConfig.class)
@TestExecutionListeners([TransactionDbUnitTestExecutionListener, DependencyInjectionTestExecutionListener])
class ItemRepositorySpec extends Specification {

    @Autowired
    private ItemRepository repository

    @DatabaseSetup(value = "classpath:database/set_user.xml", connection = "dbUnitDatabaseConnection")
    def "Should create new Item with generated id"() {
        given:
        User user = User.builder()
                .id(1L)
                .name("Name")
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
        def id = repository.save(item).getId()

        then:
        id > 0
        def res = repository.findById(id).get()
        res.getId() == id
        res.getName() == item.getName()
        res.getDescription() == item.getDescription()
        res.isAvailable() == item.isAvailable()

        with(res.getOwner()) {
            name == user.getName()
            email == user.getEmail()
            id == user.getId()
        }
    }

    @DatabaseSetup(value = "classpath:database/set_items_and_users.xml", connection = "dbUnitDatabaseConnection")
    def "Should return all items by owner id"() {
        when:
        def items = repository.findByOwnerId(1L)

        then:
        with(items) {
            id == [1, 3]
            name == ["Item_1_User_1", "Item_2_User_1"]
            description == ["Item_1_User_1", "Item_2_User_1"]
            available == [true, false]
            it.owner.id == [1L,1L]
            it.owner.name == ["User1", "User1"]
            it.owner.email == ["user1@mail.mail", "user1@mail.mail"]
        }
        with(items.get(0).getComments()) {
            id == [1]
            text == ["Comment to Item 1"]
            author.id == [2]
            created == ([LocalDateTime.of(2023,3,1,12,0,0,0)])
        }
    }

    @DatabaseSetup(value = "classpath:database/set_items_and_users.xml", connection = "dbUnitDatabaseConnection")
    def "Should return Items founded by text" () {
        when:
        def items = repository.searchByText("Item_2_User_1", PageRequest.of(0, 20))

        then:
        with(items) {
            id == [3]
            name == ["Item_2_User_1"]
            description == ["Item_2_User_1"]
            available == [false]
            it.owner.id == [1L]
            it.owner.name == ["User1"]
            it.owner.email == ["user1@mail.mail"]
        }
    }

    @DatabaseSetup(value = "classpath:database/set_item_bookings_comments.xml", connection = "dbUnitDatabaseConnection")
    def "Should return Items by owner with last booking and next booking" () {
        when:
        def items =
                repository.itemsWithNextAndPrevBookings(1,
                        LocalDateTime.of(2023, 3, 3, 12, 0, 0), null,
                        0, 20)

        then:
        with(items) {
            id == [1, 3, 4]
        }
    }
}