package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private static final String NDF_MESS_FILM = "Not found film with id = ";
    private static final String NDF_MESS_USER = "Not found like from film with userId = ";
    private final FilmStorage filmStorage;

    private Film getAndCheckFilm(Long id) {
        Film film = filmStorage.getById(id);

        if (film == null) {
            throw new NotFoundException(NDF_MESS_FILM + id);
        }

        return film;
    }

    public Film addFilm(Film film) {
        filmStorage.save(film);
        return film;
    }

    public Film updateFilm(Film film) {
        Film oldFilm = getAndCheckFilm(film.getId());

        return filmStorage.save(film);
    }

    public Film getFilmById(Long id) {
        return getAndCheckFilm(id);
    }

    public void addFilmLike(Long filmId, Long userId) {
        Film film = getAndCheckFilm(filmId);

        film.getLikes().add(userId);
        filmStorage.save(film);
    }

    public void deleteFilmLike(Long filmId, Long userId) {
        Film film = getAndCheckFilm(filmId);

        boolean isDeleted = film.getLikes().remove(userId);

        if (!isDeleted) {
            throw new NotFoundException(NDF_MESS_USER + userId);
        }

        filmStorage.save(film);
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }
}
