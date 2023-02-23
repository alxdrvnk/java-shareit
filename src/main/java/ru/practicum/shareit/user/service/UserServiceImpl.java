package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao storage;

    @Override
    public User create(User user) {
        return storage.create(user);
    }

    @Override
    public User update(User updateUser, long userId) {
        User user = UserMapper.patchUser(updateUser, getUserBy(userId));
        if (storage.update(user) == 0) {
                throw new ShareItAlreadyExistsException(
                        String.format("User with email: %s already exists", user.getEmail()));
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return storage.getAll();
    }

    @Override
    public User getUserBy(long id) {
        return storage.getBy(id).orElseThrow(
                () -> new ShareItNotFoundException(String.format("User with id: %s not found", id)));
    }

    @Override
    public void deleteUserBy(long id) {
        if (storage.deleteBy(id) == 0) {
            throw new ShareItNotFoundException(
                    String.format("User with id: %d not found", id));
        }
    }

}
