package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserLikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void likeFilm(long userId, long filmId) {
        log.info("Пользователь с id = " + userId + " пытается лайкнуть фильм с id = " + filmId);
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLike(long userId, long filmId) {
        log.info("Пользователь с id = " + userId + " пытается удалить свой лайк для фильма с id = " + filmId);
        Film film = filmStorage.getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new UserLikeNotFoundException("Лайк от пользователя с id = " + userId + " фильму с id = " + filmId + " не найден");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Пытаемся запросить данные о " + count + " самых популярных фильмов");
        List<Film> popularFilms = filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        if (popularFilms.size() != count) {
            log.info("Найдено только " + popularFilms.size() + " фильмов");
        }
        return popularFilms;
    }
}
