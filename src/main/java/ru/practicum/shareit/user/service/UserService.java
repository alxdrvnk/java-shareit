package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(User updateUser, long userId);

    List<User> getAllUsers();

    User getUserBy(long id);

    void deleteUserBy(long id);
}
