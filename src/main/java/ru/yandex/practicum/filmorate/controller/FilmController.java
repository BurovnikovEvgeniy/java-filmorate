package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Поступил запрос на добавление нового фильма");
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updateFilm) {
        log.info("Поступил запрос на обновление данных о фильме");
        return filmStorage.updateFilm(updateFilm);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Поступил запрос на получения всех данных о фильмах");
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable String id) {
        log.info("Поступил запрос на получение фильма по id");
        return filmStorage.getFilmById(Long.parseLong(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable String id, @PathVariable String userId) {
        log.info("Поступил запрос на добавление лайка фильму от пользователя");
        filmService.likeFilm(Long.parseLong(userId), Long.parseLong(id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable String id, @PathVariable String userId) {
        log.info("Поступил запрос на удаление лайка фильму от пользователя");
        filmService.deleteLike(Long.parseLong(userId), Long.parseLong(id));
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") String count) {
        log.info("Поступил запрос на получение данных о самых популярных фильмов");
        return filmService.getPopularFilms(Integer.parseInt(count));
    }
}
