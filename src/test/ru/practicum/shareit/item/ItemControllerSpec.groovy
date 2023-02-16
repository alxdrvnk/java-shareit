package ru.practicum.shareit.item

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import ru.practicum.shareit.item.dto.ItemDto
import ru.practicum.shareit.user.User
import ru.practicum.shareit.user.dto.UserDto
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerSpec extends Specification {

    @Autowired
    private MockMvc mvc

    private final ObjectMapper objectMapper = new ObjectMapper()

    def "Should return 200 when create item"() {
        given:
        def userDto = UserDto.builder()
                .name("testUser")
                .email("testUser@email.email")
                .build()

        def itemDto = ItemDto.builder()
                .name("item1")
                .description("item1 description")
                .available(true)
                .owner(User.builder().name("testUser").email("testUser@email.email").build())
                .request(null)
                .build()

        expect:
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())

        mvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
    }
}
