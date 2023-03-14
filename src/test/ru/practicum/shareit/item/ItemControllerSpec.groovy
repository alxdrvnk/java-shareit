package ru.practicum.shareit.item

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import ru.practicum.shareit.exceptions.ShareItExceptionHandler
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.item.dto.ItemDto
import ru.practicum.shareit.item.mapper.CommentMapper
import ru.practicum.shareit.item.mapper.CommentMapperImpl
import ru.practicum.shareit.item.mapper.ItemMapper
import ru.practicum.shareit.item.mapper.ItemMapperImpl
import ru.practicum.shareit.item.model.Item
import ru.practicum.shareit.item.service.ItemService
import ru.practicum.shareit.user.model.User
import ru.practicum.shareit.user.dto.UserDto
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
class ItemControllerSpec extends Specification {

    private final ObjectMapper objectMapper = new ObjectMapper()
    private final ItemMapper itemMapper = new ItemMapperImpl()
    private final CommentMapper commentMapper = new CommentMapperImpl()

    def "Should return 200 when create item"() {
        given:
        def service = Mock(ItemService)
        def controller = new ItemController(service, itemMapper, commentMapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def itemDto = ItemDto.builder()
                .name("item1")
                .description("item1 description")
                .available(true)
                .build()

        when:
        def request = post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString((itemDto)))
                .header("X-Sharer-User-Id", 1)

        and:
        server.perform(request)
                .andExpect(status().isOk())

        then:
        1 * service.create(_ as Item, 1L)
    }


    def "Should return 404 when add item with non-existent user"() {
        given:
        def service = Mock(ItemService)
        def controller = new ItemController(service, itemMapper, commentMapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def itemDto = ItemDto.builder()
                .name("item2")
                .description("item2 description")
                .available(true)
                .build()

        when:
        def request = post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString((itemDto)))
                .header("X-Sharer-User-Id", 9999)

        and:
        server.perform(request)
                .andExpect(status().isNotFound())

        then:
        1 * service.create(_ as Item, 9999L) >> { throw new ShareItNotFoundException("") }
    }

    def "Should return 200 when update item"() {
        given:
        def service = Mock(ItemService)
        service.getItemByUser(1L, 1L) >> {
            return Item.builder()
                    .id(1L)
                    .name("Item1")
                    .description("Item1")
                    .owner(User.builder().build())
                    .build()
        }
        def controller = new ItemController(service, itemMapper, commentMapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def updateItemDto = ItemDto.builder()
                .name("UpdateItemName")
                .description("UpdateItemName description")
                .available(false)
                .build()

        when:
        def request = patch("/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateItemDto))
                .header("X-Sharer-User-Id", 1)
        and:
        server.perform(request)
                .andExpect(status().isOk())

        then:
        interaction {
            1 * service.update(updateItemDto, 1, 1)
        }
    }
}
