package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface InMemoryRepository<T extends BaseUnit> {
    public T getById(long id);

    public List<T> getAll();

    public void add(T baseUnit);
}
