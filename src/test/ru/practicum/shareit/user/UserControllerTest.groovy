package ru.practicum.shareit.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException
import ru.practicum.shareit.exceptions.ShareItExceptionHandler
import ru.practicum.shareit.user.dto.UserDto
import ru.practicum.shareit.user.mapper.UserMapperImpl
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.service.UserService
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@Subject(UserController)
class UserControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    private final ObjectMapper objectMapper = new ObjectMapper()

    def "Should return 200 when create new user"() {
        given:
        def mapper = new UserMapperImpl()
        def service = Mock(UserService)
        def controller = new UserController(service, mapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def userDto = UserDto.builder()
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
        def mapper = new UserMapperImpl()
        def service = Mock(UserService)
        def controller = new UserController(service, mapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def userDto = UserDto.builder()
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
        def mapper = new UserMapperImpl()
        def service = Mock(UserService)
        def controller = new UserController(service, mapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def userDto = UserDto.builder()
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
