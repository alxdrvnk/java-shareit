package ru.practicum.shareit.booking.repository

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.apache.catalina.startup.ContextConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
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
    def "Should return Bookings find by owner id" () {
        when:
        def bookingList = bookingRepository.findAllByOwnerId(1L)

        then:
        bookingList.size() > 0
        with(bookingList) {
            id == [5, 8, 7, 4, 6, 2, 1]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Bookings find by owner id and past state" () {
        when:
        def bookingList = bookingRepository.
                findAllByOwnerIdAndPastState(
                        1L,
                        LocalDateTime.of(2023,3,7,12,0,0))

        then:
        bookingList.size() > 0
        with(bookingList) {
            id == [2, 1]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Booking find by owner id and future state" () {
        when:
        def bookingList = bookingRepository.
                findAllByOwnerIdAndFutureState(
                        1L,
                        LocalDateTime.of(2023,3,5,12,0,0))

        then:
        bookingList.size() > 0
        with(bookingList) {
            id == [5, 8, 7, 4, 6]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Booking find by owner id and current state" () {
        when:
        def bookingList = bookingRepository.
                findAllByOwnerIdAndCurrentState(
                        1L,
                        LocalDateTime.of(2023,3,9,12,0,0))

        then:
        bookingList.size() > 0
        with(bookingList) {
            id == [8, 7, 4, 6]
        }
    }

    @DatabaseSetup(
            value = "classpath:database/set_booking.xml",
            connection = "dbUnitDatabaseConnection")
    def "Should return Booking find by owner id and status" () {
        when:
        def bookingWaitingList = bookingRepository.
                findAllByOwnerIdAndStatus(1L, BookingStatus.WAITING)
        def bookingApprovedList = bookingRepository.
                findAllByOwnerIdAndStatus(1L, BookingStatus.APPROVED)
        def bookingRejectedList = bookingRepository.
                findAllByOwnerIdAndStatus(1L, BookingStatus.REJECTED)
        def bookingCanceledList = bookingRepository.
                findAllByOwnerIdAndStatus(1L, BookingStatus.CANCELED)
        then:
        bookingWaitingList.size() > 0
        bookingApprovedList.size() > 0
        bookingRejectedList.size() > 0
        bookingCanceledList.size() > 0

        with(bookingWaitingList) {
            id == [5]
        }

        with(bookingApprovedList) {
            id == [4,6,2,1]
        }

        with(bookingRejectedList) {
            id == [7]
        }

        with(bookingCanceledList) {
            id == [8]
        }
    }
}
