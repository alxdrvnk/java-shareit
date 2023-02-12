package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ShareitNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage storage;

    public UserDto create(User user) {
        return UserMapper.toUserDto(storage.create(user));
    }

    public UserDto update(User user) {
        if (storage.update(user) == 0) {
            throw new ShareitNotFoundException(
                    String.format("User with id: %d not found", user.getId()));
        }
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return storage.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUserBy(Long id) {
        User user = (User) storage.getBy(id).orElseThrow(
                () -> new ShareitNotFoundException(String.format("User with id: %d not found", id)));
        return UserMapper.toUserDto(user);
    }

    public void deleteUserBy(Long id) {
        if (storage.deleteBy(id) == 0) {
            throw new ShareitNotFoundException(
                    String.format("User with id: %d not found", id));
        }
    }
}
