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

    @Query(value = "SELECT i.*, t.start_date, t.end_date FROM ITEMS i\n" +
            "INNER JOIN\n" +
            "((SELECT b.* FROM BOOKINGS b\n" +
            "WHERE b.END_DATE  < ?2\n" +
            "ORDER BY b.END_DATE DESC \n" +
            "LIMIT 1)\n" +
            "UNION ALL(\n" +
            "SELECT b2.* FROM BOOKINGS b2 \n" +
            "WHERE b2.START_DATE > ?2) LIMIT 1) AS t ON t.item_id = i.id\n" +
            "WHERE i.OWNER_ID = ?1", nativeQuery = true)
    List<Object> findByOwnerIdWithNextAndPrevBookings(long userId, LocalDateTime date);
}
