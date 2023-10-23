package ru.yandex.practicum.filmorate.model.validation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LaterStartDateRealeasedFilmValidator
    implements ConstraintValidator<LaterStartDateRealeasedFilm, LocalDate> {
    private static final LocalDate START_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return value.isAfter(START_DATE);
    }
}
