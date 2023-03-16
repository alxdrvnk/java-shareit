package ru.practicum.shareit.item.repository.impl.transformer;

import org.hibernate.transform.ResultTransformer;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemResponseDtoTransformer implements ResultTransformer {

    private final Map<Long, ItemResponseDto> dtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);
        Long itemResponseId = Long.valueOf((Integer) tuple[aliasToIndexMap.get("cur_item_id")]);

        BookingItemDto lastBooking = getBookingDto(
                "last_booking_id",
                "last_booking_start",
                "last_booking_end",
                "last_booker_id",
                tuple, aliasToIndexMap);

        BookingItemDto nextBooking = getBookingDto(
                "next_booking_id",
                "next_booking_start",
                "next_booking_end",
                "next_booker_id",
                tuple, aliasToIndexMap);

        CommentResponseDto comment = getCommentDto(
                "comment_id",
                "comment_text",
                "comment_author_name",
                "comment_created",
                tuple, aliasToIndexMap);

        User owner = getUser(
                "item_owner_id",
                "item_owner_name",
                "item_owner_email",
                tuple, aliasToIndexMap);

        ItemResponseDto itemResponseDto = dtoMap.computeIfAbsent(
                itemResponseId,
                id -> ItemResponseDto.builder()
                        .id(itemResponseId)
                        .name((String) tuple[aliasToIndexMap.get("item_name")])
                        .description((String) tuple[aliasToIndexMap.get("item_description")])
                        .available((Boolean) tuple[aliasToIndexMap.get("item_available")])
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

    private Map<String, Integer> aliasToIndexMap(String[] aliases) {
        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i].toLowerCase(), i);
        }
        return aliasToIndexMap;
    }

    private BookingItemDto getBookingDto(String idAlias,
                                         String startAlias,
                                         String endAlias,
                                         String bookerAlias,
                                         Object[] tuple,
                                         Map<String, Integer> aliasToIndexMap) {

        if (tuple[aliasToIndexMap.get(idAlias)] == null) {
            return null;
        }

        return BookingItemDto.builder()
                .id(Long.valueOf((Integer) tuple[aliasToIndexMap.get(idAlias)]))
                .bookerId(Long.valueOf((Integer) tuple[aliasToIndexMap.get(bookerAlias)]))
                .startDate(((Timestamp) tuple[aliasToIndexMap.get(startAlias)]).toLocalDateTime())
                .endDate(((Timestamp) tuple[aliasToIndexMap.get(endAlias)]).toLocalDateTime())
                .build();
    }

    private CommentResponseDto getCommentDto(String idAlias,
                                             String textAlias,
                                             String authorAlias,
                                             String createDateAlias,
                                             Object[] tuple,
                                             Map<String, Integer> aliasToIndexMap) {

        if (tuple[aliasToIndexMap.get(idAlias)] == null) {
            return null;
        }

        return CommentResponseDto.builder()
                .id(Long.valueOf((Integer) tuple[aliasToIndexMap.get(idAlias)]))
                .authorName((String) tuple[aliasToIndexMap.get(authorAlias)])
                .text((String) tuple[aliasToIndexMap.get(textAlias)])
                .created(((Timestamp) tuple[aliasToIndexMap.get(createDateAlias)]).toLocalDateTime())
                .build();
    }

    private User getUser(String idAlias,
                         String nameAlias,
                         String emailAlias,
                         Object[] tuple,
                         Map<String, Integer> aliasToIndexMap) {

        if (tuple[aliasToIndexMap.get(idAlias)] == null) {
            return null;
        }

        return User.builder()
                .id(Long.valueOf((Integer) tuple[aliasToIndexMap.get(idAlias)]))
                .name((String) tuple[aliasToIndexMap.get(nameAlias)])
                .email((String) tuple[aliasToIndexMap.get(emailAlias)])
                .build();
    }
}
