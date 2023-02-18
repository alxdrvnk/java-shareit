package ru.practicum.shareit.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import ru.practicum.shareit.user.dto.UserDto
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
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

        expect:
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict())
    }

    def "Should return 409 when update user email already exists"() {
        given:
        def userDto = UserDto.builder()
                .name("newUser")
                .email("newUser@email.email")
                .build()

        def updateUserDto = UserDto.builder()
                .email("user1@email.email")
                .build()

        expect:
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())

        mvc.perform(patch("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isConflict())
    }
}
