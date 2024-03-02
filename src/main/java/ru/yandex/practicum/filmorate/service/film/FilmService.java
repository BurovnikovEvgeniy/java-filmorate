package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserLikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenreFilmsDao;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeDao likeDao;
    private final MpaDao mpaDao;
    private final GenreFilmsDao genreFilmsDao;

    @Autowired
    public FilmService(FilmStorage filmStorage, LikeDao likeDao, MpaDao mpaDao, GenreFilmsDao genreFilmsDao) {
        this.filmStorage = filmStorage;
        this.likeDao = likeDao;
        this.mpaDao = mpaDao;
        this.genreFilmsDao = genreFilmsDao;
    }

    public Film createFilm(Film film) {
        log.info("Пытаемся создать новый фильм");
        Film addFilm = filmStorage.addFilm(film);
        genreFilmsDao.addGenresForFilmId(addFilm.getId(), film.getGenres());
        addFilm.getGenres().addAll(film.getGenres());
        likeDao.addLikesForFilmId(addFilm.getId(), film.getLikes());
        addFilm.getLikes().addAll(film.getLikes());
        return addFilm;
    }

    public Film updateFilm(Film film) {
        log.info("Пытаемся обновить данные о фильме");
        Film updateFilm = filmStorage.updateFilm(film);
        genreFilmsDao.updateGenresByFilmId(updateFilm.getId(), film.getGenres());
        updateFilm.getGenres().addAll(genreFilmsDao.getGenresByFilmId(updateFilm.getId()));
        likeDao.updateLikesByFilmId(updateFilm.getId(), film.getLikes());
        updateFilm.getLikes().addAll(film.getLikes());
        return updateFilm;
    }

    public List<Film> getFilms() {
        log.info("Пытаемся получить все данные о фильмах");
        List<Film> list = filmStorage.getAllFilms();
        list.forEach(film -> {
            film.getGenres().addAll(genreFilmsDao.getGenresByFilmId(film.getId()));
            film.getLikes().addAll(likeDao.getLikesByFilmId(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        });
        return list;
    }

    public Film getFilmById(long id) {
        log.info("Пытаемся получить все данные о фильме по id = " + id);
        Film film = filmStorage.getFilmById(id);
        film.getGenres().addAll(genreFilmsDao.getGenresByFilmId(film.getId()));
        film.getLikes().addAll(likeDao.getLikesByFilmId(film.getId()));
        film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        return film;
    }

    public void likeFilm(long userId, long filmId) {
        log.info("Пользователь с id = " + userId + " пытается лайкнуть фильм с id = " + filmId);
        likeDao.addLike(filmId, userId);
    }

    public void deleteLike(long userId, long filmId) {
        log.info("Пользователь с id = " + userId + " пытается удалить свой лайк для фильма с id = " + filmId);
        List<Long> usersLikeFilm = likeDao.getLikesByFilmId(filmId);
        if (!usersLikeFilm.contains(userId)) {
            throw new UserLikeNotFoundException("Лайк от пользователя с id = " + userId + " фильму с id = " + filmId + " не найден");
        }
        likeDao.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Пытаемся запросить данные о " + count + " самых популярных фильмов");

        List<Film> popularFilms = getFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        if (popularFilms.size() != count) {
            log.info("Найдено только " + popularFilms.size() + " фильмов");
        }
        return popularFilms;
    }
}
