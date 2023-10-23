package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.InMemoryRepository;
import ru.yandex.practicum.filmorate.repository.inmemory.InMemoryBaseRepository;
import ru.yandex.practicum.filmorate.repository.inmemory.exception.NotFoundException;
import java.util.List;

@Service
public class FilmService {
    private final InMemoryRepository filmRepository;

    public FilmService() {
        filmRepository = new InMemoryBaseRepository<Film>();
    }

    public Film addFilm(Film film) {
        filmRepository.save(film);
        return film;
    }

    public Film updateFilm(Film film) {
        Film oldFilm = (Film) filmRepository.getById(film.getId());

        if (oldFilm == null) {
            throw new NotFoundException("Not found with id =" + film.getId());
        }

        filmRepository.deleteById(film.getId());
        filmRepository.save(film);

        return (Film) filmRepository.save(film);
    }

    public List<Film> getAllFilms() {
        return filmRepository.getAll();
    }
}
