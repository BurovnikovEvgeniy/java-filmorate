package ru.yandex.practicum.filmorate.model;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    private Validator() {
    }

    public static void isValid(final User user) {
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            LOGGER.debug("Электронная почта невалидна");
            throw new ValidationException("Электронная почта невалидна");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            LOGGER.debug("Логин не может быть пустым");
            throw new ValidationException("Логин невалиден");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            LOGGER.debug("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения невалидна");
        }
        LOGGER.debug("Данные пользователя валидны");
    }

    public static void isValid(final Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            LOGGER.debug("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма невалидно");
        }
        if (film.getDescription().length() > 200) {
            LOGGER.debug("Превышена максимальная длина описания — 200 символов");
            throw new ValidationException("Описание фильма невалидно");
        }
        if (film.getReleaseDate().isBefore(Film.OLDEST_FILM)) {
            LOGGER.debug("Фильм не может быть снят раньше " + Film.OLDEST_FILM);
            throw new ValidationException("Дата премьеры фильма невалидна");
        }
        if (film.getDuration() <= 0) {
            LOGGER.debug("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма невалидна");
        }
        LOGGER.debug("Данные фильма валиды");
    }
}
