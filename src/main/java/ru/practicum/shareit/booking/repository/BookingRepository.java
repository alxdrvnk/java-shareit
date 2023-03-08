package ru.practicum.shareit.booking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdAndItemId(long userId, long itemId);

    List<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> findAllByBookerIdAndStatusIsOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStartAfter(long userId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndEndBefore(long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(long userId);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId AND b.end < :pastDate " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndPastState(long userId, LocalDateTime pastDate);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId AND b.start > :futureDate " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndFutureState(long userId, LocalDateTime futureDate);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId AND b.start > :date AND b.end < :date " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndCurrentState(long userId, LocalDateTime date);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStatus(long userId, BookingStatus status);

}
