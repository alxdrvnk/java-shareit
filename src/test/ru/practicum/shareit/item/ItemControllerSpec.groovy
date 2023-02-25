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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
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


    def "Should return 404 when add item with non-existent user"() {
        given:
        def itemDto = ItemDto.builder()
                .name("item2")
                .description("item2 description")
                .available(true)
                .owner(null)
                .request(null)
                .build()

        expect:
        mvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 9999)
                .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isNotFound())
    }

    def "Should return 200 when update item"() {
        given:
        def updateItemDto = ItemDto.builder()
                .id(1)
                .name("UpdateItemName")
                .description("UpdateItemName description")
                .available(false)
                .build()

        expect:
        mvc.perform(patch("/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(objectMapper.writeValueAsString(updateItemDto)))
                .andExpect(status().isOk())

    }

    def "Should return 404 when item update from other user"() {
        given:
        def updateItemDto = ItemDto.builder()
                .id(1)
                .name("UpdateItemName")
                .description("UpdateItemName description")
                .available(false)
                .build()

        expect:
        mvc.perform(patch("/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 2)
                .content(objectMapper.writeValueAsString(updateItemDto)))
                .andExpect(status().isNotFound())

    }

    def "Should return 200 and empty list when empty search"() {
        expect:
        mvc.perform(get("/items/search?text=")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(Collections.emptyList() as String))
    }
}
