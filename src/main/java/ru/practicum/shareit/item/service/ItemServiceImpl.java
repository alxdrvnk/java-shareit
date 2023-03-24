package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ShareItBadRequest;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Slf4j(topic = "ItemService")
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final Clock clock;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Item create(Item item, long userId) {
        User user = userService.getUserBy(userId);
        return itemRepository.save(item.withOwner(user));
    }

    @Transactional
    @Override
    public Item update(ItemDto itemDto, long itemId, long userId) {
        Item actualItem = getItemByUser(userId, itemId);
        Item item = itemMapper.updateItemFromDto(itemDto, actualItem.toBuilder());
        return itemRepository.save(item);
    }

    @Override
    public ItemResponseDto getItemById(long id, long userId) {
        if (itemRepository.existsByIdAndOwnerId(id, userId))
            return getItemForOwner(userId, id);
        else {
            return itemMapper.toItemDto(itemRepository.findById(id).orElseThrow(
                    () -> new ShareItNotFoundException(
                            String.format("Item with id: %s not found", id))));
        }
    }

    @Override
    public Item getItemByUser(long userId, long itemId) {
        userService.getUserBy(userId);
        return itemRepository.findByIdAndOwnerId(itemId, userId).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Item with id: %s does not belong to User with id: %s", itemId, userId)));
    }

    @Override
    public Item getItemNotOwnedByUser(long userId, long itemId) {
        return itemRepository.findByIdAndOwnerIdNot(itemId, userId).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Item with id: %d not found", itemId)));
    }

    @Override
    public List<ItemResponseDto> getItemsForOwner(long userId) {
        return itemRepository.itemsWithNextAndPrevBookings(userId,
                LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS), null);
    }

    @Override
    public ItemResponseDto getItemForOwner(long userId, long itemId) {
        List<ItemResponseDto> items =
                itemRepository.itemsWithNextAndPrevBookings(userId,
                        LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS), itemId);

        if (items.isEmpty()) {
            throw new ShareItNotFoundException(
                    String.format("Item with id: %d not found", itemId));
        }

        return items.get(0);
    }

    @Transactional
    @Override
    public void deleteItemById(long itemId, long userId) {
        itemRepository.deleteByIdAndOwnerId(itemId, userId);
    }

    @Override
    public List<ItemResponseDto> getByText(long userId, String text) {
        userService.getUserBy(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemMapper.toItemDtoList(itemRepository.searchByText(text));
    }

    @Override
    @Transactional
    public Comment addComment(long userId, long itemId, CommentRequestDto dto) {
        User user = userService.getUserBy(userId);
        Item item = itemMapper.toItem(getItemById(itemId, userId));
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemId(userId, itemId);

        if (bookings.isEmpty()) {
            throw new ShareItNotFoundException(
                    String.format("User with Id: %d don't booking Item with Id: %d", userId, itemId));
        }

        if (bookings.get(0).getEnd().isAfter(LocalDateTime.now(clock))) {
            throw new ShareItBadRequest("Booking time not ended");
        }

        return commentRepository.save(
                commentMapper.toComment(dto, user, item, LocalDateTime.now(clock).plusSeconds(1L)));
    }
}