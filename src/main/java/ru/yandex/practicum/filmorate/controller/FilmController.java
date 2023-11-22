package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Request for get all films");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        log.info("Request getFilmById");
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilm(@RequestParam(name = "count", required = false, defaultValue = "10") Long count) {
        log.info("Request getPopularFilm count = {}", count);
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film addFilm(@Validated @RequestBody Film film) {
        log.info("Request addFilm {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Validated @RequestBody Film film) {
        log.info("Request updateFilm {}", film);
        if (film.getId() == null) {
            throw new ValidationException("Film with empty Id");
        }
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addFilmLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        log.info("Request addFilmLike to film id = {}, user id = {}", filmId, userId);
        filmService.addFilmLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteFilmLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        log.info("Request deleteFilmLike to film id = {}, user id = {}", filmId, userId);
        filmService.deleteFilmLike(filmId, userId);
    }

}
