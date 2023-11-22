package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryLikeStorage implements LikeStorage {
    private final Map<Long, Set<Long>> friendMap;

    public InMemoryLikeStorage() {
        this.friendMap = new HashMap<>();
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        Set<Long> likes = friendMap.get(filmId);

        if (likes == null) {
            likes = new HashSet<>();

        }

        if (userId != null) {
            likes.add(userId);
        }

        friendMap.put(filmId, likes);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        Set<Long> likes = friendMap.get(filmId);

        if (likes == null) {
            return false;
        }

        return likes.remove(userId);
    }

    @Override
    public Map<Long, Set<Long>> getAll() {
        return friendMap;
    }

    @Override
    public List<Long> getPopularFilms(Long count) {
        return friendMap.keySet().stream()
                .sorted((filmKey1, filmKey2) -> friendMap.get(filmKey2).size() - friendMap.get(filmKey1).size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
