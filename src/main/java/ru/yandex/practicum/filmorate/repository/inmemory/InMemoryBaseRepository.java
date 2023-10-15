package ru.yandex.practicum.filmorate.repository.inmemory;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.repository.InMemoryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@NoArgsConstructor
public class InMemoryBaseRepository<T extends BaseUnit> implements InMemoryRepository<T> {

    private final Map<Long, T> inMemoryMap = new HashMap<>();

    @Override
    public T getById(long id) {
        return inMemoryMap.get(id);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(inMemoryMap.values());
    }

    @Override
    public void add(T baseUnit) {
        inMemoryMap.put(baseUnit.getId(), baseUnit);
    }

}
