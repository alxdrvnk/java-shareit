package ru.practicum.shareit.request

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import ru.practicum.shareit.exceptions.ShareItExceptionHandler
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.request.dto.ItemRequestCreationDto
import ru.practicum.shareit.request.mapper.ItemRequestMapper
import ru.practicum.shareit.request.model.ItemRequest
import ru.practicum.shareit.request.service.ItemRequestService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ItemRequestControllerSpec extends Specification {

    private final ObjectMapper objectMapper = new ObjectMapper()
    private final ItemRequestMapper itemRequestMapper = new ItemRequestMapper()

    def "Should return 200 when create ItemRequest"() {
        given:
        def service = Mock(ItemRequestService)
        def controller = new ItemRequestController(itemRequestMapper, service)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()


        and:
        def itemRequestCreationDto = ItemRequestCreationDto.builder()
                .description("Test description")
                .build()

        when:
        def request = post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString((itemRequestCreationDto)))
                .header("X-Sharer-User-Id", 1)

        and:
        server.perform(request)
                .andExpect(status().isOk())

        then:
        1 * service.create(_ as ItemRequest, 1L)
    }

    def "Should return 404 when add ItemRequest with non-existent user"() {
        given:
        def service = Mock(ItemRequestService)
        def controller = new ItemRequestController(itemRequestMapper, service)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def itemRequestDto = ItemRequestCreationDto.builder()
                .description("Test")
                .build()

        when:
        def request = post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString((itemRequestDto)))
                .header("X-Sharer-User-Id", 9999)

        and:
        server.perform(request)
                .andExpect(status().isNotFound())

        then:
        1 * service.create(_ as ItemRequest, 9999L) >> { throw new ShareItNotFoundException("") }
    }
}