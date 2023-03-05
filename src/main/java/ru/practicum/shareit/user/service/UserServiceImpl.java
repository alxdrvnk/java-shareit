package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException;
import ru.practicum.shareit.exceptions.ShareItNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = ShareItAlreadyExistsException.class)
    @Override
    public User create(User user) {
//        validateEmail(user.getEmail());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(UserDto userDto, long userId) {
        User user = UserMapper.MAPPER.updateUserFromDto(userDto, getUserBy(userId).toBuilder());
        return userRepository.save(user);
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
        userRepository.deleteById(id);
    }
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ShareItAlreadyExistsException("Email already exists");
        }
    }
}
