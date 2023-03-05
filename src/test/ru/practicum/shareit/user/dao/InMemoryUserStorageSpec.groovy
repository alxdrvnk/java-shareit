package ru.practicum.shareit.user.dao

import ru.practicum.shareit.user.model.User
import spock.lang.Specification

class InMemoryUserStorageSpec extends Specification {

    HashMap<String, User> users
    HashMap<Long, String> idMailMap
    InMemoryUserStorage inMemory

    def setup() {
        users = new HashMap<>()
        idMailMap = new HashMap<>()
        inMemory = new InMemoryUserStorage(users, idMailMap)
    }

    def "Should return User with Id when create new user"() {
        given:
        def user = User.builder()
                .name("user")
                .email("user@email.email")
                .build()

        when:
        inMemory.create(user)

        then:
        users.size() == 1
        idMailMap.size() == 1

        users.get("user@email.email") == user.withId(1)
        idMailMap.get(1L) == "user@email.email"
    }

    def "Should return 0 when update user email already exists"() {
        given:
        def user1 = User.builder()
                .name("user1")
                .email("user1@email.email")
                .build()

        def user2 = User.builder()
                .name("user2")
                .email("user2@email.email")
                .build()

        def updateUser2 = User.builder()
                .id(2)
                .name("user2")
                .email("user1@email.email")
                .build()

        and:
        inMemory.create(user1)
        inMemory.create(user2)

        expect:
        inMemory.update(updateUser2) == 0
    }

    def "Should return 0 when update non-exists user"() {
        given:
        def updateUser = User.builder()
                .id(9999)
                .name("user")
                .email("user@email.email")
                .build()

        expect:
        inMemory.update(updateUser) == 0
    }

    def "Should return empty when get non-exist user"() {
        expect:
        inMemory.getBy(9999) == Optional.empty()
    }

    def "Should return 0 when delete non-expect user"() {
        given:
        def user = User.builder()
                .email("test@mail.mail")
                .build()
        and:
        inMemory.create(user)

        expect:
        inMemory.deleteBy(9999) == 0
    }
}
