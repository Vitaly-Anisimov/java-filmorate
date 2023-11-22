package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFriendStorage implements FriendStorage {
    private final Map<Long, Set<Long>> friendMap;

    public InMemoryFriendStorage() {
        this.friendMap = new HashMap<>();
    }

    @Override
    public void save(Long userId, Long friendId) {
        Set<Long> friends = friendMap.get(userId);

        if (friends == null) {
            friends = new HashSet<>();
        }

        if (friendId != null) {
            friends.add(friendId);
        }
        friendMap.put(userId, friends);
    }

    @Override
    public boolean delete(Long userId, Long friendId) {
        Set<Long> friends = friendMap.get(userId);

        if (friends == null) {
            return false;
        }

        return friends.remove(friendId);
    }

    @Override
    public Set<Long> getFriendsByUserId(Long userId) {
        return friendMap.get(userId);
    }

    @Override
    public List<Long> getCommonFriends(Long userId, Long friendId) {
        return friendMap.get(userId).stream()
                .filter(sortFriend -> friendMap.get(friendId).contains(sortFriend))
                .collect(Collectors.toList());
    }
}
