package ru.practicum.shareit.booking.repository

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification

@TestExecutionListeners([TransactionDbUnitTestExecutionListener, DependencyInjectionTestExecutionListener])
class BookingRepositorySpec extends Specification {

    @Autowired
    private BookingRepository bookingRepository;

    @DatabaseSetup(connection = "dbUnitDatabaseConnection")
    def "Should create new Booking with generated id" () {

    }
}
