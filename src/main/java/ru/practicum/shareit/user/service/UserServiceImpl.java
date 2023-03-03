package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ShareItAlreadyExistsException("Ошибка добавления пользователя");
        }
    }

    @Override
    public User update(User updateUser, long userId) {
        try {
            User user = UserMapper.patchUser(updateUser, getUserBy(userId));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ShareItAlreadyExistsException("Ошибка обновления пользователя");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserBy(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ShareItNotFoundException(String.format("User with id: %s not found", id)));
    }

    @Override
    public void deleteUserBy(long id) {
        userRepository.deleteById(id);
    }
}
