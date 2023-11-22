package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getById(long id);

    List<User> getAll();

    User save(User baseUnit);

    void deleteById(long id);
}
