package ru.practicum.shareit.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException
import ru.practicum.shareit.exceptions.ShareItExceptionHandler
import ru.practicum.shareit.user.dto.UserGatewayDto
import ru.practicum.shareit.user.mapper.UserMapper
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.service.UserService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest extends Specification {

    private final ObjectMapper objectMapper = new ObjectMapper()
    private final UserMapper mapper = new UserMapper()

    def "Should return 200 when create new user"() {
        given:
        def service = Mock(UserService)
        def controller = new UserController(service, mapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def userDto = UserGatewayDto.builder()
                .name("user1")
                .email("user1@email.email")
                .build()

        when:
        def request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))

        and:
        server.perform(request)
                .andExpect(status().isOk())

        then:
        1 * service.create(_)
    }

    def "Should return 409 when create user with duplicated email"() {
        given:
        def service = Mock(UserService)
        def controller = new UserController(service, mapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def userDto = UserGatewayDto.builder()
                .name("user1")
                .email("user1@email.email")
                .build()

        when:
        def request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))

        and:
        server.perform(request)
                .andExpect(status().isConflict())

        then:
        interaction {
            1 * service.create(_ as User) >> { throw new ShareItAlreadyExistsException("")}
        }
    }

    def "Should return 409 when update user email already exists"() {
        given:
        def service = Mock(UserService)
        def controller = new UserController(service, mapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def userDto = UserGatewayDto.builder()
                .name("user1")
                .email("user1@email.email")
                .build()

        when:
        def request = patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))

        and:
        server.perform(request)
                .andExpect(status().isConflict())

        then:
        interaction {
            1 * service.update(userDto, 1L) >> { throw new ShareItAlreadyExistsException("")}
        }
    }
}
