package ru.practicum.shareit.user.service

import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.dao.UserDao
import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    def "Should throw ShareItAlreadyExistsException if updated email already exists"() {
        given:
        def user = User.builder()
                .name("testUser")
                .email("testUser@email.email")
                .build()

        def dao = Stub(UserDao) {
            update(user) >> 0

            getBy(1L) >> Optional.of(User.builder().build())
        }

        def service = new UserServiceImpl(dao)

        when:
        service.update(user, 1L)

        then:
        thrown(ShareItAlreadyExistsException)
    }

    def "Should throw ShareItNotFoundException if get non-existing user" () {
        given:
        def dao = Stub(UserDao) {
            getBy(_ as Long) >> Optional.empty()
        }

        def service = new UserServiceImpl(dao)

        when:
        service.getUserBy(9999)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should throw ShareItNotFoundException if delete non-existing user" () {
        given:

        def dao = Stub(UserDao) {
            deleteBy(_ as Long) >> 0
        }

        def service = new UserServiceImpl(dao)

        when:
        service.deleteUserBy(9999)

        then:
        thrown(ShareItNotFoundException)
    }
}