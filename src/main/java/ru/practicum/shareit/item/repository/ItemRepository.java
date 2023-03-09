package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.lang.annotation.Native;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByIdAndOwnerId(Long id, Long userId);

    List<Item> findByOwnerId(Long id);

    void deleteByIdAndOwnerId(Long id, Long userId);

    @Query("SELECT i FROM Item i " +
            "WHERE upper(i.name) LIKE upper( concat('%', ?1, '%')) " +
            "OR upper(i.description) LIKE upper( concat('%', ?1, '%')) AND i.available = true")
    List<Item> searchByText(String text);

    Optional<Item> findByIdAndOwnerIdNot(long itemId, long userId);

    @Query(value = "SELECT DISTINCT ON(id) * FROM (\n" +
            "SELECT i.*,\n" +
            "b.id AS prev_booking_id,\n" +
            "b.start_date AS prev_booking_start,\n" +
            "b.end_date AS prev_booking_end,\n" +
            "b.booker_id AS prev_booking_booker_id,\n" +
            "b2.id AS next_booking_id,\n" +
            "b2.start_date AS next_booking_start,\n" +
            "b2.end_date AS next_booking_end,\n" +
            "b2.booker_id AS next_booking_booker_id FROM items i \n" +
            "INNER JOIN BOOKINGS b ON b.item_id = i.id AND b.end_date <= ?2 AND b.STATUS = 'APPROVED'\n" +
            "LEFT JOIN BOOKINGS b2 ON b2.item_id = i.id AND b2.start_date >= ?2 AND b2.STATUS = 'APPROVED'\n" +
            "WHERE i.owner_id = ?1\n" +
            "ORDER BY b.END_DATE DESC, b2.START_DATE ASC)", nativeQuery = true)
    List<Object> findByOwnerIdWithNextAndPrevBookings(long userId, LocalDateTime date);
}
