package ru.practicum.shareit.user.mapper

import ru.practicum.shareit.user.dto.UserDto
import ru.practicum.shareit.user.model.User
import spock.lang.Specification

class UserMapperSpec extends Specification {

    UserMapper mapper = new UserMapperImpl()

    def "Should map User to UserDto"() {
        given:
        User user = User.builder()
                .id(1L)
                .name("Name")
                .email("mail@mail.mail")
                .build()

        UserDto expect = UserDto.builder()
                .id(1)
                .name("Name")
                .email("mail@mail.mail")
                .build()

        when:
        def dto = mapper.toUserDto(user)

        then:
        dto == expect

    }

    def "Should partial map User from UserDto"() {
        given:
        User user = User.builder()
                .id(1L)
                .name("Name")
                .email("mail@mail.mail")
                .build()

        UserDto updateNameDto = UserDto.builder()
                .name("Update")
                .build()

        UserDto updateEmailDto = UserDto.builder()
                .email("update@update.mail")
                .build()

        when:
        def expectUserUpdateName = mapper.updateUserFromDto(updateNameDto, user.toBuilder())
        def expectUserUpdateEmail = mapper.updateUserFromDto(updateEmailDto, user.toBuilder())

        then:
        expectUserUpdateName.id == 1
        expectUserUpdateName.name == "Update"
        expectUserUpdateName.email == "mail@mail.mail"

        expectUserUpdateEmail.id == 1
        expectUserUpdateEmail.name == "Name"
        expectUserUpdateEmail.email == "update@update.mail"
    }

}
