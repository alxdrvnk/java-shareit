package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByIdAndOwnerId(Long id, Long userId);

    Page<Item> findByOwnerId(Long id, Pageable pageable);

    void deleteByIdAndOwnerId(Long id, Long userId);

    @Query("SELECT i FROM Item i " +
            "WHERE upper(i.name) LIKE upper( concat('%', ?1, '%')) " +
            "OR upper(i.description) LIKE upper( concat('%', ?1, '%')) AND i.available = true")
    List<Item> searchByText(String text, Pageable pageable);

    Optional<Item> findByIdAndOwnerIdNot(long itemId, long userId);

    boolean existsByIdAndOwnerId(long id, long userId);
}
