package ru.practicum.shareit.user.service

import org.springframework.dao.DataIntegrityViolationException
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.user.dto.UserDto
import ru.practicum.shareit.user.mapper.UserMapper
import ru.practicum.shareit.user.mapper.UserMapperImpl
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.repository.UserRepository
import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    private final UserMapper mapper = new UserMapperImpl()

    def "Should throw ShareItAlreadyExistsException if updated email already exists"() {
        given:
        def userDto = UserDto.builder()
                .name("Name")
                .email("mail@mail.mail")
                .build()

        def user = User.builder()
                .id(2L)
                .name("Name")
                .email("new@mail.mail")
                .build()

        def repository = Stub(UserRepository) {
            save(_ as User) >> { throw new DataIntegrityViolationException("") }
            findById(_ as Long) >> Optional.of(user)
        }

        def service = new UserServiceImpl(repository, mapper)

        when:
        service.update(userDto, 2L)

        then:
        thrown(ShareItAlreadyExistsException)
    }

    def "Should throw ShareItNotFoundException if get non-existing user"() {
        given:
        def dao = Stub(UserRepository) {
            findById(_ as Long) >> Optional.empty()
        }

        def service = new UserServiceImpl(dao, mapper)

        when:
        service.getUserBy(9999)

        then:
        thrown(ShareItNotFoundException)
    }

    def "Should throw ShareItNotFoundException if delete non-existing user"() {
        given:
        def dao = Stub(UserRepository) {
            deleteById(_ as Long) >> { throw new IllegalArgumentException("") }
        }

        def service = new UserServiceImpl(dao, mapper)

        when:
        service.deleteUserBy(9999)

        then:
        thrown(ShareItNotFoundException)
    }
}