package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ShareItBadRequest;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
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
    public Item getItemById(long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new ShareItNotFoundException(
                        String.format("Item with id: %s not found", id)));
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
    public List<Item> getItemsByUser(long userId) {
        var data = itemRepository.findByOwnerIdWithNextAndPrevBookings(userId, LocalDateTime.now());
        return itemRepository.findByOwnerId(userId);
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
    public Comment addComment(long userId, long itemId, CommentRequestDto dto) {
        User user = userService.getUserBy(userId);
        Item item = getItemById(itemId);
        List<Booking> bookings =  bookingRepository.findAllByBookerIdAndItemId(userId, itemId);

       if (bookings.isEmpty()) {
           throw new ShareItNotFoundException(
                   String.format("User with Id: %d don't booking Item with Id: %d", userId, itemId));
       }

       if (bookings.stream().findFirst().get().getEnd().isAfter(LocalDateTime.now())) {
           throw new ShareItBadRequest("Booking time not ended");
       }

       return commentRepository.save(
               commentMapper.toComment(dto, user, item, LocalDateTime.now()));
    }
}
