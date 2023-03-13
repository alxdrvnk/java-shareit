package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Transactional(rollbackFor = ShareItAlreadyExistsException.class)
    @Override
    public User create(User user) {
//        validateEmail(user.getEmail());
        User userDb;
        try {
           userDb = userRepository.save(user);
        } catch (Exception e) {
            log.error("Failed to create User {}", user);
            throw new ShareItAlreadyExistsException("Create User Error: email already exists");
        }
        return userDb;
    }

    @Transactional
    @Override
    public User update(UserDto userDto, long userId) {
        User user;
        try {
            user = mapper.updateUserFromDto(userDto, getUserBy(userId).toBuilder());
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to update User with Id {}", userId);
            throw new ShareItAlreadyExistsException("Update User Error: Email already exists");
        }
        return user;
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

    @Transactional
    @Override
    public void deleteUserBy(long id) {
        try {
            userRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ShareItNotFoundException(
                    String.format("User with Id: %d not found", id));
        }
    }
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ShareItAlreadyExistsException("Email already exists");
        }
    }
}
