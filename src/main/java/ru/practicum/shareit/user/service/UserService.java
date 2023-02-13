package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ShareitNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(User user);

    List<User> getAllUsers();

    User getUserBy(long id);

    void deleteUserBy(long id);
}
