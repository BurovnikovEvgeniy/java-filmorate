package ru.yandex.practicum.filmorate.models;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.BaseTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmTest extends BaseTest {

    @Test
    public void createValidFilm() {
        Film user = Film.builder()
                .id(1L)
                .name("Green Book")
                .description("Путешествие итальянца-вышибалы и чернокожего пианиста по Америке 1960-х.")
                .releaseDate(LocalDate.of(2018, 9, 11))
                .duration(130)
                .build();
        assertDoesNotThrow(() -> Validator.isValid(user));
    }

    @Test
    public void createFilmWithIncorrectName() {
        Film user = Film.builder()
                .id(1L)
                .name("")
                .description("Путешествие итальянца-вышибалы и чернокожего пианиста по Америке 1960-х.")
                .releaseDate(LocalDate.of(2018, 9, 11))
                .duration(130)
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.isValid(user));
        assertEquals("Название фильма невалидно", exception.getMessage());
    }

    @Test
    public void createFilmWithIncorrectDescription() {
        Film user = Film.builder()
                .id(1L)
                .name("Green Book")
                .description("Путешествие итальянца-вышибалы и чернокожего пианиста по Америке 1960-х. " +
                        "И еще очень много много много много много много много много много много много много " +
                        "много много всего интересного преинтересного")
                .releaseDate(LocalDate.of(2018, 9, 11))
                .duration(130)
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.isValid(user));
        assertEquals("Описание фильма невалидно", exception.getMessage());
    }

    @Test
    public void createFilmWithIncorrectReleaseDate() {
        Film user = Film.builder()
                .id(1L)
                .name("Green Book")
                .description("Путешествие итальянца-вышибалы и чернокожего пианиста по Америке 1960-х.")
                .releaseDate(LocalDate.of(618, 9, 11))
                .duration(130)
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.isValid(user));
        assertEquals("Дата премьеры фильма невалидна", exception.getMessage());
    }

    @Test
    public void createFilmWithIncorrectDuration() {
        Film user = Film.builder()
                .id(1L)
                .name("Green Book")
                .description("Путешествие итальянца-вышибалы и чернокожего пианиста по Америке 1960-х.")
                .releaseDate(LocalDate.of(2018, 9, 11))
                .duration(0)
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.isValid(user));
        assertEquals("Продолжительность фильма невалидна", exception.getMessage());
    }
}
