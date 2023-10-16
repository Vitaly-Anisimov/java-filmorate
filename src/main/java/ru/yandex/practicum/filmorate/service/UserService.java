package ru.yandex.practicum.filmorate.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryRepository;
import ru.yandex.practicum.filmorate.repository.inmemory.InMemoryBaseRepository;
import ru.yandex.practicum.filmorate.repository.inmemory.exception.NotFoundException;

import java.util.List;

@Service
@NoArgsConstructor

public class UserService {
    private InMemoryRepository userRepository = new InMemoryBaseRepository<User>();
    private long idValue = 0;

    private long getNextValueSequince() {
        return ++idValue;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextValueSequince());
        userRepository.add(user);
        return user;
    }

    public User updateUser(User user) {
        User oldUser = (User) userRepository.getById(user.getId());

        if (oldUser == null) {
            throw new NotFoundException("Not found with id =" + user.getId());
        }

        if (user.getName().isBlank() || user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        oldUser.setEmail(user.getEmail());
        oldUser.setLogin(user.getLogin());
        oldUser.setName(user.getName());
        oldUser.setBirthday(user.getBirthday());

        return oldUser;
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }
}
