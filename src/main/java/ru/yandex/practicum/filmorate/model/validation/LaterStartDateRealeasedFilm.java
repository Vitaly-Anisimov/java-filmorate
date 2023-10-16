package ru.yandex.practicum.filmorate.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = LaterStartDateRealeasedFilmValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface LaterStartDateRealeasedFilm {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
