package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Set;

public interface FriendStorage {
    void save(Long userId, Long friendId);

    boolean delete(Long userId, Long friendId);

    Set<Long> getFriendsByUserId(Long userId);

    List<Long> getCommonFriends(Long userId, Long friendId);
}
