package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> inMemoryMap;
    private long idValue;

    public InMemoryFilmStorage() {
        this.inMemoryMap = new HashMap<>();
        this.idValue = 0;
    }

    private long getNextValueSequince() {
        return ++idValue;
    }

    @Override
    public Film getById(long id) {
        return inMemoryMap.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(inMemoryMap.values());
    }

    @Override
    public Film save(Film film) {
        if (film.getId() == null) {
            film.setId(getNextValueSequince());
        }

        inMemoryMap.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteById(long id) {
        inMemoryMap.remove(id);
    }
}
