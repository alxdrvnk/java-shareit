package ru.practicum.shareit.booking.repository

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import ru.practicum.shareit.booking.model.BookingStatus
import ru.practicum.shareit.configuration.DbUnitConfig
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
@ContextConfiguration(classes = DbUnitConfig.class)
@TestExecutionListeners([TransactionDbUnitTestExecutionListener, DependencyInjectionTestExecutionListener])
class BookingRepositorySpec extends Specification {

    @Autowired
    private BookingRepository bookingRepository

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Bookings find by owner id"() {
        when:
        def bookingList = bookingRepository.findAllByOwnerId(1L, PageRequest.of(0, 20))

        then:
        bookingList.size() > 0
        with(bookingList) {
            id == [5, 6, 8, 7, 4, 2, 1]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Bookings find by owner id and past state"() {
        when:
        def bookingList = bookingRepository.
                findAllByOwnerIdAndPastState(
                        1L,
                        LocalDateTime.of(2023, 3, 7, 12, 0, 0),
                        PageRequest.of(0, 20))

        then:
        bookingList.size() > 0
        with(bookingList) {
            id == [2, 1]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Booking find by owner id and future state"() {
        when:
        def bookingList = bookingRepository.
                findAllByOwnerIdAndFutureState(
                        1L,
                        LocalDateTime.of(2023, 3, 5, 12, 0, 0),
                        PageRequest.of(0, 20))

        then:
        bookingList.size() > 0
        with(bookingList) {
            id == [5, 6, 8, 7, 4]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Booking find by owner id and current state"() {
        when:
        def bookingList = bookingRepository.
                findAllByOwnerIdAndCurrentState(
                        1L,
                        LocalDateTime.of(2023, 3, 9, 12, 0, 0),
                        PageRequest.of(0, 20))

        then:
        bookingList.size() > 0
        with(bookingList) {
            id == [8, 7]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Booking find by owner id and status"() {
        given:
        def pageable = PageRequest.of(0, 20)
        when:
        def bookingWaitingList = bookingRepository.
                findAllByOwnerIdAndStatus(1L, BookingStatus.WAITING, pageable)
        def bookingApprovedList = bookingRepository.
                findAllByOwnerIdAndStatus(1L, BookingStatus.APPROVED, pageable)
        def bookingRejectedList = bookingRepository.
                findAllByOwnerIdAndStatus(1L, BookingStatus.REJECTED, pageable)
        def bookingCanceledList = bookingRepository.
                findAllByOwnerIdAndStatus(1L, BookingStatus.CANCELED, pageable)
        then:
        bookingWaitingList.size() > 0
        bookingApprovedList.size() > 0
        bookingRejectedList.size() > 0
        bookingCanceledList.size() > 0

        with(bookingWaitingList) {
            id == [5]
        }

        with(bookingApprovedList) {
            id == [6, 4, 2, 1]
        }

        with(bookingRejectedList) {
            id == [7]
        }

        with(bookingCanceledList) {
            id == [8]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return list of last Bookings when send List of items id"() {
        given:
        def itemIdList = List.of(1)
        when:
        def bookingList = bookingRepository.
                findLastBookingForItems(itemIdList,
                        LocalDateTime.of(2023, 3, 5, 12, 00, 0))

        then:
        bookingList.size() > 0

        bookingList.get(0).getId() == 2
        bookingList.get(0).getItemId() == 1
        bookingList.get(0).getBookerId() == 4
        bookingList.get(0).getStartDate() == LocalDateTime.of(2023, 3, 4, 12, 0, 0)
        bookingList.get(0).getEndDate() == LocalDateTime.of(2023, 3, 6, 12, 0, 0)
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return list of next Bookings when send List of items id"() {
        given:
        def itemIdList = List.of(4)
        when:
        def bookingList = bookingRepository.
                findNextBookingForItems(itemIdList,
                        LocalDateTime.of(2023, 3, 9, 12, 15, 0))

        then:
        bookingList.size() > 0

        bookingList.get(0).getId() == 6
        bookingList.get(0).getItemId() == 4
        bookingList.get(0).getBookerId() == 4
        bookingList.get(0).getStartDate() == LocalDateTime.of(2023, 3, 10, 12, 0, 0)
        bookingList.get(0).getEndDate() == LocalDateTime.of(2023, 3, 11, 12, 0, 0)
    }

}
