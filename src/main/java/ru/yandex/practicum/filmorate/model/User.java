package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.customJsonFormat.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.model.customJsonFormat.LocalDateSerialize;

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
    @JsonSerialize(using = LocalDateSerialize.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;
}
