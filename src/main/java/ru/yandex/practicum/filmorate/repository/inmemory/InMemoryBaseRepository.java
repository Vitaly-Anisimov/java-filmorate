package ru.yandex.practicum.filmorate.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.repository.InMemoryRepository;

import java.util.*;

@Repository
public class InMemoryBaseRepository<T extends BaseUnit> implements InMemoryRepository<T> {
    private final Map<Long, T> inMemoryMap;
    private long idValue;

    public InMemoryBaseRepository() {
        this.inMemoryMap = new LinkedHashMap<>();
        this.idValue = 0;
    }

    private long getNextValueSequince() {
        return ++idValue;
    }

    @Override
    public T getById(long id) {
        return inMemoryMap.get(id);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(inMemoryMap.values());
    }

    @Override
    public T save(T baseUnit) {
        if (baseUnit.getId() == null) {
            baseUnit.setId(getNextValueSequince());
        }

        inMemoryMap.put(baseUnit.getId(), baseUnit);
        return baseUnit;
    }

    @Override
    public void deleteById(long id) {
        inMemoryMap.remove(id);
    }
}
