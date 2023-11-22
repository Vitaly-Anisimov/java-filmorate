package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static final String NDF_MESS_FILM = "Not found film with id = ";
    private static final String NDF_MESS_USER = "Not found like from film with userId = ";
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    private Film getAndCheckFilm(Long id) {
        Film film = filmStorage.getById(id);

        if (film == null) {
            throw new NotFoundException(NDF_MESS_FILM + id);
        }

        return film;
    }

    public Film addFilm(Film film) {
        filmStorage.save(film);
        likeStorage.addLike(film.getId(), null);
        log.info("Added film {} ", film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        Film oldFilm = getAndCheckFilm(film.getId());
        Film newFilm = filmStorage.save(film);

        log.info("Updated film {}", newFilm.getId());
        return newFilm;
    }

    public Film getFilmById(Long id) {
        Film film = getAndCheckFilm(id);

        log.info("Founded film {}", film.getId());
        return film;
    }

    public void addFilmLike(Long filmId, Long userId) {
        Film film = getAndCheckFilm(filmId);

        likeStorage.addLike(film.getId(), userId);
        log.info("Added like to film {} userId {}", film.getId(), userId);
    }

    public void deleteFilmLike(Long filmId, Long userId) {
        Film film = getAndCheckFilm(filmId);
        boolean isDeleted = likeStorage.deleteLike(filmId, userId);

        if (!isDeleted) {
            throw new NotFoundException(NDF_MESS_USER + userId);
        }

        log.info("Deleted like from film {} userId {}", film.getId(), userId);
    }

    public List<Film> getPopularFilms(Long count) {
        List<Film> popularFilms = likeStorage.getPopularFilms(count).stream()
                .map(popularFilm -> filmStorage.getById(popularFilm))
                .collect(Collectors.toList());

        log.info("SortedPopularFilms {}", popularFilms);
        return popularFilms;
    }

    public List<Film> getAllFilms() {
        List<Film> allFilms = filmStorage.getAll();

        log.info("AllFilm {}", allFilms);
        return allFilms;
    }
}
