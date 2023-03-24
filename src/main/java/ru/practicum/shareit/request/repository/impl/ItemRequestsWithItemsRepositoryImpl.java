package ru.practicum.shareit.request.repository.impl;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.repository.ItemRequestsWithItemsRepository;
import ru.practicum.shareit.request.repository.impl.transformer.ItemRequestDtoTransformer;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ItemRequestsWithItemsRepositoryImpl implements ItemRequestsWithItemsRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ItemRequestResponseDto> getByRequesterId(Long userId) {
        return entityManager.createNativeQuery(
                "SELECT " +
                        "r.id AS request_id, " +
                        "r.description AS request_description, " +
                        "r.create_date AS request_create_date, " +
                        "i.id AS item_id, " +
                        "i.name AS item_name, " +
                        "i.owner_id AS item_owner_id " +
                    "FROM requests AS r " +
                    "INNER JOIN items AS i " +
                        "ON i.request_id = r.id " +
                    "WHERE r.requester_id = :userId " +
                    "ORDER BY r.id DESC")
                .setParameter("userId", userId)
                .unwrap(Query.class)
                .setResultTransformer(new ItemRequestDtoTransformer())
                .getResultList();
    }

    @Override
    public Optional<ItemRequestResponseDto> getItemRequestById(Long requestId) {
        List<ItemRequestResponseDto> dtoList = entityManager.createNativeQuery(
                "SELECT " +
                        "r.id AS request_id, " +
                        "r.description AS request_description, " +
                        "r.create_date AS request_create_date, " +
                        "i.id AS item_id, " +
                        "i.name AS item_name, " +
                        "i.owner_id AS item_owner_id " +
                    "FROM requests AS r " +
                    "INNER JOIN items AS i " +
                        "ON i.request_id = r.id " +
                    "WHERE r.id =  :requestId")
                .setParameter("requestId", requestId)
                .unwrap(Query.class)
                .setResultTransformer(new ItemRequestDtoTransformer())
                .getResultList();

        return dtoList.stream().findFirst();
    }

    @Override
    public List<ItemRequestResponseDto> findByRequesterIdNot(long userId, int from, int size) {
        List<ItemRequestResponseDto> dtoList = entityManager.createNativeQuery(
                        "SELECT * " +
                           "FROM (" +
                                "SELECT " +
                                    "r.id AS request_id, " +
                                    "r.description AS request_description, " +
                                    "r.create_date AS request_create_date, " +
                                    "i.id AS item_id, " +
                                    "i.name AS item_name, " +
                                    "i.owner_id AS item_owner_id," +
                                    "dense_rank() OVER (ORDER BY r.create_date DESC) AS rank " +
                                "FROM requests AS r " +
                                "LEFT JOIN items AS i " +
                                    "ON i.request_id = r.id " +
                                "WHERE r.requester_id <> :userId) AS d " +
                           "WHERE d.rank >= :start AND d.rank < :end " +
                           "ORDER BY d.rank")
                .setParameter("userId", userId)
                .setParameter("start", from)
                .setParameter("end", from + size)
                .unwrap(Query.class)
                .setResultTransformer(new ItemRequestDtoTransformer())
                .getResultList();

        return dtoList;
    }
}
