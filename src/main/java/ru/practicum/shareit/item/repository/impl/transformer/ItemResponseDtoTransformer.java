package ru.practicum.shareit.item.repository.impl.transformer;

import org.hibernate.transform.ResultTransformer;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.DbResponsePars;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemResponseDtoTransformer implements ResultTransformer {

    private final Map<Long, ItemResponseDto> dtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] objects, String[] strings) {
        Map<String, Integer> aliasToIndexMap = DbResponsePars.aliasToIndexMap(strings);
        Long itemResponseId = Long.valueOf((Integer) objects[aliasToIndexMap.get("cur_item_id")]);

        BookingItemDto lastBooking = getBookingDto(
                "last_booking_id",
                "last_booking_start",
                "last_booking_end",
                "last_booker_id",
                objects, aliasToIndexMap);

        BookingItemDto nextBooking = getBookingDto(
                "next_booking_id",
                "next_booking_start",
                "next_booking_end",
                "next_booker_id",
                objects, aliasToIndexMap);

        CommentResponseDto comment = getCommentDto(
                "comment_id",
                "comment_text",
                "comment_author_name",
                "comment_created",
                objects, aliasToIndexMap);

        User owner = getUser(
                "item_owner_id",
                "item_owner_name",
                "item_owner_email",
                objects, aliasToIndexMap);

        ItemResponseDto itemResponseDto = dtoMap.computeIfAbsent(
                itemResponseId,
                id -> ItemResponseDto.builder()
                        .id(itemResponseId)
                        .name((String) objects[aliasToIndexMap.get("item_name")])
                        .description((String) objects[aliasToIndexMap.get("item_description")])
                        .available((Boolean) objects[aliasToIndexMap.get("item_available")])
                        .owner(owner)
                        .lastBooking(lastBooking)
                        .nextBooking(nextBooking)
                        .comments(new ArrayList<>())
                        .build()
        );
        if (comment != null) {
            List<CommentResponseDto> commentResponseDtoList = itemResponseDto.getComments();
            commentResponseDtoList.add(comment);
            itemResponseDto = itemResponseDto.withComments(commentResponseDtoList);
        }
        return itemResponseDto;
    }

    @Override
    public List<ItemResponseDto> transformList(List collection) {
        return new ArrayList<>(dtoMap.values());
    }


    private BookingItemDto getBookingDto(String idAlias,
                                         String startAlias,
                                         String endAlias,
                                         String bookerAlias,
                                         Object[] objects,
                                         Map<String, Integer> aliasToIndexMap) {

        if (objects[aliasToIndexMap.get(idAlias)] == null) {
            return null;
        }

        return BookingItemDto.builder()
                .id(Long.valueOf((Integer) objects[aliasToIndexMap.get(idAlias)]))
                .bookerId(Long.valueOf((Integer) objects[aliasToIndexMap.get(bookerAlias)]))
                .startDate(((Timestamp) objects[aliasToIndexMap.get(startAlias)]).toLocalDateTime())
                .endDate(((Timestamp) objects[aliasToIndexMap.get(endAlias)]).toLocalDateTime())
                .build();
    }

    private CommentResponseDto getCommentDto(String idAlias,
                                             String textAlias,
                                             String authorAlias,
                                             String createDateAlias,
                                             Object[] objects,
                                             Map<String, Integer> aliasToIndexMap) {

        if (objects[aliasToIndexMap.get(idAlias)] == null) {
            return null;
        }

        return CommentResponseDto.builder()
                .id(Long.valueOf((Integer) objects[aliasToIndexMap.get(idAlias)]))
                .authorName((String) objects[aliasToIndexMap.get(authorAlias)])
                .text((String) objects[aliasToIndexMap.get(textAlias)])
                .created(((Timestamp) objects[aliasToIndexMap.get(createDateAlias)])
                        .toLocalDateTime().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    private User getUser(String idAlias,
                         String nameAlias,
                         String emailAlias,
                         Object[] objects,
                         Map<String, Integer> aliasToIndexMap) {

        if (objects[aliasToIndexMap.get(idAlias)] == null) {
            return null;
        }

        return User.builder()
                .id(Long.valueOf((Integer) objects[aliasToIndexMap.get(idAlias)]))
                .name((String) objects[aliasToIndexMap.get(nameAlias)])
                .email((String) objects[aliasToIndexMap.get(emailAlias)])
                .build();
    }
}
