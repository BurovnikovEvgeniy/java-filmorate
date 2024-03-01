package ru.yandex.practicum.filmorate.storage.daoImpl.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Deprecated
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public Film addFilm(Film newFilm) {
        Validator.isValid(newFilm);
        newFilm.setId(idCounter++);
        filmMap.put(newFilm.getId(), newFilm);
        log.info("В базу добавлен новый фильм с id = " + newFilm.getId());
        return newFilm;
    }

    @Override
    public Film updateFilm(Film updateFilm) {
        Validator.isValid(updateFilm);
        if (!filmMap.containsKey(updateFilm.getId())) {
            log.error("Пытаемся обновить фильм с id = " + updateFilm.getId() + ", но его нет в базе фильмов");
            throw new FilmNotFoundException("Фильм не найден в базе фильмов");
        }
        filmMap.put(updateFilm.getId(), updateFilm);
        log.info("Данные фильма с id = " + updateFilm.getId() + " обновлены в базе фильмов");
        return updateFilm;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получаем данные обо всех фильмах из базы");
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!filmMap.containsKey(filmId)) {
            log.error("Пытаемся получить данные о фильме по id = " + filmId + ", но его нет в базе фильмов");
            throw new FilmNotFoundException("Фильм не найден в базе фильмов");
        }
        log.info("Получаем данные о фильме по id = " + filmId + " из базы фильмов");
        return filmMap.get(filmId);
    }
}
