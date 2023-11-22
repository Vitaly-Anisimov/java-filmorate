package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.model.validation.LaterStartDateRealeasedFilm;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
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
