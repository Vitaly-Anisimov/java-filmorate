package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> inMemoryMap;
    private long idValue;

    public InMemoryUserStorage() {
        this.inMemoryMap = new HashMap<>();
        this.idValue = 0;
    }

    private long getNextValueSequince() {
        return ++idValue;
    }

    @Override
    public User getById(long id) {
        return inMemoryMap.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(inMemoryMap.values());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(getNextValueSequince());
        }

        inMemoryMap.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(long id) {
        inMemoryMap.remove(id);
    }
}
