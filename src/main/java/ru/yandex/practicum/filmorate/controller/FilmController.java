package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/films")
public class FilmController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Long, Film> filmMap = new HashMap<>();
    private static int idCounter = 0;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public Film createFilm(@RequestBody Film film) {
        Validator.isValid(film);
        film.setId(idCounter++);
        filmMap.put(film.getId(), film);
        LOGGER.info("Добавлен новый фильм с id = " + film.getId());
        return film;
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    public Film updateUser(@RequestBody Film updateFilm) {
        Validator.isValid(updateFilm);
        if (!filmMap.containsKey(updateFilm.getId())) {
            LOGGER.error("Пытаемся обновить фильм с id = " + updateFilm.getId() + ", но его нет в базе фильмов");
            throw new FilmNotFoundException("Фильм не найден в базе фильмов");
        }
        filmMap.put(updateFilm.getId(), updateFilm);
        LOGGER.info("Данные фильма с id = " + updateFilm.getId() + " обновлены");
        return updateFilm;
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    public List<Film> getUsers() {
        LOGGER.info("Получаем данные обо всех фильмах");
        return new ArrayList<>(filmMap.values());
    }
}
