package ru.practicum.shareit.booking

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import ru.practicum.shareit.booking.dto.BookingDto
import ru.practicum.shareit.booking.dto.BookingRequestDto
import ru.practicum.shareit.booking.mapper.BookingMapper
import ru.practicum.shareit.booking.mapper.BookingMapperImpl
import ru.practicum.shareit.booking.service.BookingService
import ru.practicum.shareit.exceptions.ShareItExceptionHandler
import ru.practicum.shareit.exceptions.ShareItNotFoundException
import ru.practicum.shareit.item.dto.ItemDto
import spock.lang.Specification

import java.time.LocalDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class BookingControllerSpec extends Specification {

    private final ObjectMapper objectMapper = new ObjectMapper()
    private final BookingMapper bookingMapper = new BookingMapperImpl()

    def "should return 200 when create booking"() {
        given:
        def service = Mock(BookingService)
        def controller = new BookingController(service, bookingMapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def bookingDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1L))
                .end(LocalDateTime.now().plusDays(2L))
                .build()

        when:
        def request = post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDto))
                .header("X-Sharer-USer-Id", 1)

        and:
        server.perform(request)
                .andExpect(status().isOk())
        then:
        1 * service.create(_ as BookingRequestDto, 1L)

    }

    def "Should return 404 when add booking with non-existent user" () {
        given:
        def service = Mock(BookingService)
        def controller = new BookingController(service, bookingMapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        and:
        def bookingDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1L))
                .end(LocalDateTime.now().plusDays(2L))
                .build()

        when:
        def request = post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDto))
                .header("X-Sharer-USer-Id", 9999)

        and:
        server.perform(request)
                .andExpect(status().isNotFound())

        then:
        1 * service.create(_ as BookingRequestDto, 9999L) >> { throw new ShareItNotFoundException("") }
    }

    def "Should return 400 when get bookings with not valid pagination params" () {
        given:
        def service = Mock(BookingService)
        def controller = new BookingController(service, bookingMapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        when:
        def request = get("/bookings?from=-1&size=20")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-USer-Id", 1L)

        and:
        server.perform(request)
                .andExpect(status().isBadRequest())

        then:
        0 * service.getAllByState(_ as BookingRequestDto, _ as Long)
    }

    def "Should return 400 when not valid pagination params for get bookings" () {
        given:
        def service = Mock(BookingService)
        def controller = new BookingController(service, bookingMapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        when:
        def request = get("/bookings?from=-1&size=20")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-USer-Id", 1L)

        and:
        server.perform(request)
                .andExpect(status().isBadRequest())

        then:
        0 * service.getAllByState()
    }

    def "Should return 400 when not valid pagination params for get owners bookings " () {
        given:
        def service = Mock(BookingService)
        def controller = new BookingController(service, bookingMapper)
        def server = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ShareItExceptionHandler)
                .build()

        when:
        def request = get("/bookings/owner?from=-1&size=20")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-USer-Id", 1L)

        and:
        server.perform(request)
                .andExpect(status().isBadRequest())

        then:
        0 * service.getAllByOwnerWithState()
    }
}
