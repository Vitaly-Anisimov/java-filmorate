package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryRepository;
import ru.yandex.practicum.filmorate.repository.inmemory.InMemoryBaseRepository;
import ru.yandex.practicum.filmorate.repository.inmemory.exception.NotFoundException;

import java.util.List;

@Service

public class UserService {
    private final InMemoryRepository userRepository;

    public UserService() {
        this.userRepository = new InMemoryBaseRepository<User>();
    }

    private boolean isNameEmpty(String name) {
        return name == null || name.isBlank() || name.isEmpty();
    }

    public User addUser(User user) {
        if (isNameEmpty(user.getName())) {
            user.setName(user.getLogin());
        }

        userRepository.save(user);
        return user;
    }

    public User updateUser(User user) {
        User oldUser = (User) userRepository.getById(user.getId());

        if (oldUser == null) {
            throw new NotFoundException("Not found with id =" + user.getId());
        }

        if (isNameEmpty(user.getName())) {
            user.setName(user.getLogin());
        }

        userRepository.deleteById(user.getId());
        userRepository.save(user);

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }
}
