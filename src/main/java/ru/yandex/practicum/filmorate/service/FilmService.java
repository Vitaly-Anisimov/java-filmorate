package ru.yandex.practicum.filmorate.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.InMemoryRepository;
import ru.yandex.practicum.filmorate.repository.inmemory.InMemoryBaseRepository;
import ru.yandex.practicum.filmorate.repository.inmemory.exception.NotFoundException;

import java.util.List;

@Service
@NoArgsConstructor
public class FilmService {
    private InMemoryRepository filmRepository = new InMemoryBaseRepository<Film>();
    private long idValue = 0;

    private long getNextValueSequince() {
        return ++idValue;
    }

    public Film addFilm(Film film) {
        film.setId(getNextValueSequince());
        filmRepository.add(film);
        return film;
    }

    public Film updateFilm(Film film) {
        Film oldFilm = (Film) filmRepository.getById(film.getId());

        if (oldFilm == null) {
            throw new NotFoundException("Not found with id =" + film.getId());
        }

        oldFilm.setDescription(film.getDescription());
        oldFilm.setName(film.getName());
        oldFilm.setDuration(film.getDuration());
        oldFilm.setReleaseDate(film.getReleaseDate());

        return oldFilm;
    }

    public List<Film> getAllFilms() {
        return filmRepository.getAll();
    }
}
