package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info(String.format("UserController: create User request. Data: %s", user));
        return UserMapper.toUserDto(userService.create(UserMapper.toUser(user)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody UserDto user) {
        log.info(String.format("UserController: update User with id: %d . Data: %s", id, user));

        User dbUser = userService.getUserBy(id);

        return UserMapper.toUserDto(userService.update(UserMapper.patchUser(user, dbUser)));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return UserMapper.toDtoList(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserDto getUserBy(@PathVariable("id") Long id) {
        return UserMapper.toUserDto(userService.getUserBy(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserBy(@PathVariable("id") Long id) {
        userService.deleteUserBy(id);
        log.info(String.format("UserController: Remove User with id: %d", id));
    }
}

