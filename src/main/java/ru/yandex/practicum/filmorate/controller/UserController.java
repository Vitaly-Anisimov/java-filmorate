package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService = new UserService();

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
        return userService.updateUser(user);
    }
}
