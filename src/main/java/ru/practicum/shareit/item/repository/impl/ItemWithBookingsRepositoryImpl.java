package ru.practicum.shareit.item.repository.impl;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.repository.ItemWithBookingsRepository;
import ru.practicum.shareit.item.repository.impl.transformer.ItemResponseDtoTransformer;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemWithBookingsRepositoryImpl implements ItemWithBookingsRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ItemResponseDto> itemsWithNextAndPrevBookings(long userId, LocalDateTime date, Long itemId) {
        String filter = "WHERE i.owner_id = :owner_id ";

        if (itemId != null) {
            filter += "AND i.id = " + itemId;
        }

        return entityManager.createNativeQuery(
                "SELECT " +
                                "cur_item_id, " +
                                "item_name, " +
                                "item_description, " +
                                "item_available, " +
                                "last_booking_id, " +
                                "last_booking_start, " +
                                "last_booking_end, " +
                                "last_booker_id, " +
                                "next_booking_id, " +
                                "next_booking_start, " +
                                "next_booking_end, " +
                                "next_booker_id, " +
                                "comments_author.name AS comment_author_name, " +
                                "c.id AS comment_id, " +
                                "c.text AS comment_text, " +
                                "c.created_date AS comment_created, " +
                                "item_owner_id, " +
                                "item_owner_name, " +
                                "item_owner_email "  +
                        "FROM " +
                            "(SELECT DISTINCT ON(cur_item_id) " +
                                    "i.id AS cur_item_id, " +
                                    "i.name AS item_name, " +
                                    "i.description AS item_description, " +
                                    "i.is_available AS item_available, " +
                                    "last_booking.id AS last_booking_id, " +
                                    "last_booking.start_date AS last_booking_start, " +
                                    "last_booking.end_date AS last_booking_end, " +
                                    "last_booking.booker_id AS last_booker_id, " +
                                    "next_booking.id AS next_booking_id, " +
                                    "next_booking.start_date AS next_booking_start, " +
                                    "next_booking.end_date AS next_booking_end, " +
                                    "next_booking.booker_id AS next_booker_id, " +
                                    "item_owner.id AS item_owner_id, " +
                                    "item_owner.name AS item_owner_name, " +
                                    "item_owner.email As item_owner_email " +
                        "FROM " +
                                "items AS i " +
                        "LEFT JOIN " +
                            "BOOKINGS AS last_booking " +
                                "ON last_booking.item_id = i.id " +
                                "AND last_booking.end_date <= :date " +
                                "AND last_booking.STATUS = 'APPROVED' " +
                        "LEFT JOIN " +
                            "BOOKINGS AS next_booking " +
                                "ON next_booking.item_id = i.id " +
                                "AND next_booking.start_date >= :date " +
                            "AND next_booking.STATUS = 'APPROVED' " +
                        "INNER JOIN " +
                            "users AS item_owner " +
                                "ON i.owner_id = item_owner.id " +
                        filter +
                        "ORDER BY " +
                            "last_booking.END_DATE DESC, " +
                            "next_booking.START_DATE ASC) " +
                        "LEFT JOIN " +
                            "comments AS c " +
                                "ON c.item_id = cur_item_id " +
                        "LEFT JOIN " +
                            "users AS comments_author " +
                                "ON comments_author.id = c.author_id ")
                .setParameter("date", date)
                .setParameter("owner_id", userId)
                .unwrap(Query.class)
                .setResultTransformer(new ItemResponseDtoTransformer())
                .getResultList();
    }
}
