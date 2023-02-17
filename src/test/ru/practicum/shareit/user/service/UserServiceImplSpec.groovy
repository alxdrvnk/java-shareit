package ru.practicum.shareit.user.service

import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.user.User
import ru.practicum.shareit.user.dao.InMemoryUserStorage
import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    def "Should throw ShareItAlreadyExistsException if updated email already exists"() {
        given:
        def user = User.builder()
                .name("testUser")
                .email("testUser@email.email")
                .build()

        def user2 = User.builder()
                .name("user2")
                .email("")
                .build()

        def service = new UserServiceImpl(new InMemoryUserStorage())

        and:
        service.create(user)

        def user2Db = service.create(user2)

        user2 = User.builder()
                .id(user2Db.getId())
                .name(user2Db.getName())
                .email(user.getEmail())
                .build()

        when:
        service.update(user2)

        then:
        thrown(ShareItAlreadyExistsException)
    }

    def "Should throw ShareItNotFoundException if get non-existing user" () {
        given:
        def service = new UserServiceImpl(new InMemoryUserStorage())

        when:
        service.getUserBy(9999)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should throw ShareItNotFoundException if delete non-existing user" () {
        given:
        def service = new UserServiceImpl(new InMemoryUserStorage())

        when:
        service.deleteUserBy(9999)

        then:
        thrown(ShareItNotFoundException)
    }
}