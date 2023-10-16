package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.validation.LaterStartDateRealeasedFilm;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Film extends BaseUnit {
    @NotBlank
    private String name;

    @Size(max = 200)
    @NotBlank
    private String description;

    @LaterStartDateRealeasedFilm
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
