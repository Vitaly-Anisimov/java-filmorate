package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
public class FilmController {
    private final FilmService filmService = new FilmService();

    @GetMapping
    public List<Film> getFilms() {
        log.info("Request for get all films");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Validated @RequestBody Film film) {
        log.info("Request addFilm {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Validated @RequestBody Film film) {
        log.info("Request updateFilm {}", film);
        return filmService.updateFilm(film);
    }

}
