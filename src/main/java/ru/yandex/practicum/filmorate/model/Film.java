package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.customJsonFormat.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.model.customJsonFormat.LocalDateSerialize;
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
    @JsonSerialize(using = LocalDateSerialize.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
