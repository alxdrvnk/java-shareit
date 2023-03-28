package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingForItem;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ShareItBadRequest;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ItemRequestService itemRequestService;

    @Override
    public Item create(ItemDto itemDto, long userId) {
        User user = userService.getUserBy(userId);
        ItemRequest request = null;

        if (itemDto.getRequestId() != null) {
            request = itemRequestService.getById(itemDto.getRequestId(), userId);
        }
        return itemRepository.save(
                itemMapper.toItem(itemDto)
                        .withRequest(request)
                        .withOwner(user));
    }

    @Transactional
    @Override
    public Item update(ItemDto itemDto, long itemId, long userId) {
        Item actualItem = getItemByUser(userId, itemId);
        ItemRequest request = null;

        if (itemDto.getRequestId() != null) {
            request = itemRequestService.getById(itemDto.getRequestId(), userId);
        }
        Item item = itemMapper.updateItemFromDto(itemDto, actualItem.toBuilder(), request);
        return itemRepository.save(item);
    }

    @Override
    public Item getItemById(long id, long userId) {
        if (itemRepository.existsByIdAndOwnerId(id, userId))
            return getItemForOwner(userId, id);
        else {
            return itemRepository.findById(id).orElseThrow(
                    () -> new ShareItNotFoundException(
                            String.format("Item with id: %s not found", id)));
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
    public List<Item> getItemsForOwner(long userId, int from, int size) {
        List<Item> items = itemRepository.findByOwnerId(userId, PageRequest.of(from / size, size)).getContent();

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        return getItemsWithBookings(items);
    }

    @Override
    public Item getItemForOwner(long userId, long itemId) {

        List<Item> items = itemRepository.findByOwnerId(userId, PageRequest.of(0, 1)).getContent();

        if (items.isEmpty()) {
            throw new ShareItNotFoundException(
                    String.format("Item with id: %d not found", itemId));
        }
        return getItemsWithBookings(items).get(0);
    }

    @Transactional
    @Override
    public void deleteItemById(long itemId, long userId) {
        itemRepository.deleteByIdAndOwnerId(itemId, userId);
    }

    @Override
    public List<Item> getByText(long userId, String text, int from, int size) {
        userService.getUserBy(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(from / size, size);
        return itemRepository.searchByText(text, pageable);
    }

    @Override
    @Transactional
    public Comment addComment(long userId, long itemId, CommentRequestDto dto) {
        User user = userService.getUserBy(userId);
        Item item = getItemById(itemId, userId);
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

    private List<Item> getItemsWithBookings(List<Item> items) {
        List<BookingForItem> lastBookings = bookingRepository.findLastBookingForItems(
                items.stream().map(Item::getId).collect(Collectors.toList()),
                LocalDateTime.now(clock));
        List<BookingForItem> nextBookings = bookingRepository.findNextBookingForItems(
                items.stream().map(Item::getId).collect(Collectors.toList()),
                LocalDateTime.now(clock));

        List<Item> itemsWithBookings = new ArrayList<>(items.size());

        for (Item item : items) {
            Long itemId = item.getId();
            itemsWithBookings.add(item
                    .withLastBooking(lastBookings.stream().filter(booking -> booking.getItemId().equals(itemId)).findAny().orElse(null))
                    .withNextBooking(nextBookings.stream().filter(booking -> booking.getItemId().equals(itemId)).findAny().orElse(null)));

        }
        return itemsWithBookings;
    }
}