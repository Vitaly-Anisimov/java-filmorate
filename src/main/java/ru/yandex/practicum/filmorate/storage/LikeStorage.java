package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LikeStorage {
    void addLike(Long filmId, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    Map<Long, Set<Long>> getAll();

    List<Long> getPopularFilms(Long count);
}
