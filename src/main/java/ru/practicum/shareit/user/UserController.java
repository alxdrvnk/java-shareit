package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ShareItAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info(String.format("UserController: create User request. Data: %s", user));
        return mapper.toUserDto(userService.create(mapper.toUser(user)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody UserDto user) {
        log.info(String.format("UserController: update User with id: %d . Data: %s", id, user));
        return mapper.toUserDto(userService.update(user, id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return mapper.toUserDtoList(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserDto getUserBy(@PathVariable("id") Long id) {
        return mapper.toUserDto(userService.getUserBy(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserBy(@PathVariable("id") Long id) {
        userService.deleteUserBy(id);
        log.info(String.format("UserController: Remove User with id: %d", id));
    }
}

