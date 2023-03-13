package ru.practicum.shareit.user.repository

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import ru.practicum.shareit.configuration.DbUnitConfig
import ru.practicum.shareit.user.model.User
import spock.lang.Specification


@DataJpaTest
@ContextConfiguration(classes = DbUnitConfig.class)
@TestExecutionListeners([TransactionDbUnitTestExecutionListener, DependencyInjectionTestExecutionListener])
class ItemRepositorySpec extends Specification {

    @Autowired
    private UserRepository repository

    def "Should create new User with generated id"() {
        given:
        def user = User.builder()
                .name("Name")
                .email("mail@mail.mail;")
                .build()

        when:
        def id = repository.save(user).getId()

        then:
        id > 0
        def res = repository.findById(id).get()
        res.getId() == id
        res.getName() == user.getName()
        res.getEmail() == user.getEmail()
    }


    @DatabaseSetup(value = "classpath:database/set_user.xml", connection = "dbUnitDatabaseConnection")
    def "Should return user when find by email"() {
        when:
        def user = repository.findUserByEmail("mail@mail.mail").get()

        then:
        user.getId() == 1L
        user.getName() == "Name"
        user.getEmail() == "mail@mail.mail"
    }
}
