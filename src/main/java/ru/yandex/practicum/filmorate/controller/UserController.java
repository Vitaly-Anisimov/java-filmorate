package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("Request for get all users");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Validated @RequestBody User user) {
        log.info("Request addUser {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Validated @RequestBody User user) {
        log.info("Request updateUser {}", user);
        if (user.getId() == null) {
            throw new ValidationException("User with empty Id");
        }
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllUserFriends(@PathVariable("id") Long id) {
        return userService.getAllUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long parentUser, @PathVariable("otherId") Long priorId) {
        return userService.getCommonFriends(parentUser, priorId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriendUsers(@PathVariable("friendId") Long friendId, @PathVariable("id") Long id) {
        userService.addToFriendUsers(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriendUsers(@PathVariable("friendId") Long friendId, @PathVariable("id") Long id) {
        userService.deleteFromFriendUsers(id, friendId);
    }
}
