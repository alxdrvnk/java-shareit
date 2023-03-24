package ru.practicum.shareit.request.repository.impl.transformer;

import org.hibernate.transform.ResultTransformer;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.utils.DbResponsePars;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemRequestDtoTransformer implements ResultTransformer {

    private final Map<Long, ItemRequestResponseDto> dtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] objects, String[] strings) {
        Map<String, Integer> aliasToIndexMap = DbResponsePars.aliasToIndexMap(strings);
        Long itemRequestId = Long.valueOf((Integer) objects[aliasToIndexMap.get("request_id")]);
        String itemRequestDescription = (String) objects[aliasToIndexMap.get("request_description")];
        LocalDateTime itemRequestCreated = ((Timestamp) objects[aliasToIndexMap.get("request_create_date")])
                .toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);

        ItemForItemRequestDto item = getItemForItemRequestDto(
                "item_id",
                "item_name",
                "item_owner_id", objects, aliasToIndexMap);

        ItemRequestResponseDto itemRequestResponseDto = dtoMap.computeIfAbsent(
                itemRequestId,
                id -> ItemRequestResponseDto.builder()
                        .id(itemRequestId)
                        .description(itemRequestDescription)
                        .created(itemRequestCreated)
                        .items(new ArrayList<>())
                        .build()
        );

        if (item != null) {
            List<ItemForItemRequestDto> items = itemRequestResponseDto.getItems();
            items.add(item);
            itemRequestResponseDto = itemRequestResponseDto.withItems(items);
        }
        return itemRequestResponseDto;
    }

    @Override
    public List<ItemRequestResponseDto> transformList(List list) {
        return new ArrayList<>(dtoMap.values());
    }

    private ItemForItemRequestDto getItemForItemRequestDto(String idAlias,
                                                           String nameAlias,
                                                           String ownerIdAlias,
                                                           Object[] objects,
                                                           Map<String, Integer> aliasToIndexMap) {
        if (objects[aliasToIndexMap.get(idAlias)] == null) {
            return null;
        }

        return ItemForItemRequestDto.builder()
                .id(Long.valueOf((Integer) objects[aliasToIndexMap.get(idAlias)]))
                .name((String) objects[aliasToIndexMap.get(nameAlias)])
                .ownerId(Long.valueOf((Integer) objects[aliasToIndexMap.get(ownerIdAlias)]))
                .build();
    }

}
