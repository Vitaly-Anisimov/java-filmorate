package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseUnit {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;


    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
