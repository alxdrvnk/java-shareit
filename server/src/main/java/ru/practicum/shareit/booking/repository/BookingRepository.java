package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingForItem;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdAndItemId(long userId, long itemId);

    List<Booking> findAllByBookerIdOrderByStartDesc(long userId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                             LocalDateTime startDate,
                                                                             LocalDateTime endDate,
                                                                             Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusIsOrderByStartDesc(long userId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(long userId, Pageable pageable);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId AND b.end < :pastDate " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndPastState(long userId, LocalDateTime pastDate, Pageable pageable);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId AND b.start > :futureDate " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndFutureState(long userId, LocalDateTime futureDate, Pageable pageable);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId AND b.start < :date AND b.end > :date " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndCurrentState(long userId, LocalDateTime date, Pageable pageable);

    @Query("SELECT b FROM Booking As b " +
            "INNER JOIN Item as i ON i.id = b.item.id " +
            "WHERE i.owner.id = :userId AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStatus(long userId, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking As b " +
            "WHERE b.id = :id " +
            "AND (b.booker.id = :userId OR b.item.owner.id = :userId)")
    Optional<Booking> findByIdAndUserId(long id, long userId);

    @Query(value = "SELECT DISTINCT ON(b.ITEM_ID) " +
            "b.id AS id, " +
            "b.booker_id AS bookerId, " +
            "b.item_id AS itemId," +
            "b.start_date AS startDate, " +
            "b.end_date AS endDate " +
            "FROM BOOKINGS b " +
            "WHERE b.ITEM_ID IN :itemIdList " +
            "AND (b.END_DATE <= :date " +
            "OR b.START_DATE < :date AND b.END_DATE > :date) " +
            "AND b.STATUS = 'APPROVED' " +
            "ORDER BY b.item_id, b.END_DATE DESC", nativeQuery = true)
    List<BookingForItem> findLastBookingForItems(List<Long> itemIdList, LocalDateTime date);

    @Query(value = "SELECT DISTINCT ON(b.ITEM_ID) " +
            "b.id AS id, " +
            "b.booker_id AS bookerId, " +
            "b.item_id AS itemId," +
            "b.start_date AS startDate, " +
            "b.end_date AS endDate " +
            "FROM BOOKINGS b " +
            "WHERE b.ITEM_ID IN :itemIdList " +
            "AND b.START_DATE >= :date " +
            "AND b.STATUS = 'APPROVED' " +
            "ORDER BY b.item_id, b.START_DATE ASC", nativeQuery = true)
    List<BookingForItem> findNextBookingForItems(List<Long> itemIdList, LocalDateTime date);
}
