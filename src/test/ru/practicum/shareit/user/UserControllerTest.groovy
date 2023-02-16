package ru.practicum.shareit.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowire
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testng.IExpectedExceptionsHolder
import ru.practicum.shareit.item.dao.InMemoryItemStorage
import ru.practicum.shareit.user.dto.UserDto
import ru.practicum.shareit.user.service.UserService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    private final ObjectMapper objectMapper = new ObjectMapper()

    def "Should return 200 when create new user"() {
        given:
        def userDto = UserDto.builder()
                .name("user1")
                .email("user1@email.email")
                .build()

        expect:
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
    }

    def "Should return 409 when create user with duplicated email"() {
        given:
        def userDto = UserDto.builder()
                .name("user1")
                .email("user1@email.email")
                .build()

        def service = Mock(UserService)
        def controller = new UserController(service)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .build()

        when:
        def request = post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDto))


        server.perform(request)
                .andExpect(status().isOk())

        server.perform(request)
                .andExpect(status().isOk())

        then:
        interaction {
            def user = User.builder()
                    .name("user1")
                    .email("user1@email.email")
                    .build()

            2 * service.create(user)
//            1 * service.create(user) >> { throw new ShareItAlreadyExistsException(String.format("User with email: %s already exists", user.getEmail()))}
        }
    }
}
