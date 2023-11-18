package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.model.validation.LaterStartDateRealeasedFilm;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Film extends BaseUnit {
    @Builder.Default
    Set<Long> likes = new HashSet<>();
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
