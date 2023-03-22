package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(UserDto userDto, long userId);

    List<User> getAllUsers();

    User getUserBy(long id);

    void deleteUserBy(long id);
}
