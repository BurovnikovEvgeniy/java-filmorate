package ru.yandex.practicum.filmorate.storage.daoImpl.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AddNewDataInDBException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UpdateDataInDBException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        Validator.isValid(film);
        log.info("Добавляем информацию о новом фильме в базу фильмов");
        try {
            jdbcTemplate.update("INSERT INTO films (name, description, release_date, duration, id_rating) VALUES(?, ?, ?, ?, ?)",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId()
            );
        } catch (DataAccessException e) {
            log.error("Добавление данных о фильме было завершилось неудачно");
            throw new AddNewDataInDBException("Данные о фильме с id = " + film.getId());
        }

        log.debug("В базу добавлен новый фильм с id = " + film.getId());

        log.info("Запросим данные о новом фильме id = " + film.getId());
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM films " +
                            "WHERE name=? AND description=? " +
                            "AND release_date=? " +
                            "AND duration=? " +
                            "AND id_rating=?",
                    new FilmMapper(),
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId()
            );
        } catch (DataAccessException e) {
            log.error("Данные о только что добавленном фильме не были получены");
            throw new FilmNotFoundException("Ошибка получения данных о фильме после добавления а базу");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        Validator.isValid(film);
        getFilmById(film.getId());
        log.info("Обновляем данные о фильме " + film.getId());
        try {
            jdbcTemplate.update("UPDATE films "
                            + "SET name=?, description=?, release_date=?, duration=?, id_rating=?"
                            + "WHERE film_id=?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
        } catch (DataAccessException e) {
            log.error("Данные о фильме не были обновлены");
            throw new UpdateDataInDBException("Ошибка обновления данных о фильме");
        }
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получаем данные обо всех фильмах из базы");
        try {
            return new ArrayList<>(jdbcTemplate.query("SELECT * FROM films", new FilmMapper()));
        } catch (DataAccessException e) {
            log.error("Данные обо всех фильмах не были получены из базы");
            throw new FilmNotFoundException("Ошибка получения данных обо всех фильмах из базы");
        }
    }

    @Override
    public Film getFilmById(Long filmId) {
        try {
            Film film = jdbcTemplate.queryForObject("SELECT * FROM films WHERE film_id=?", new FilmMapper(), filmId);
            log.info("Получаем данные о фильме по id = " + filmId + " из базы фильмов");
            return film;
        } catch (DataAccessException e) {
            log.error("Пытаемся получить данные о фильме по id = " + filmId + ", но его нет в базе фильмов");
            throw new FilmNotFoundException("Фильм не найден в базе фильмов");
        }
    }

    static class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Mpa mpa = new Mpa();
            mpa.setId(resultSet.getLong("id_rating"));
            return Film.builder()
                    .id(resultSet.getLong("film_id"))
                    .name(resultSet.getString("name"))
                    .description(resultSet.getString("description"))
                    .releaseDate(resultSet.getDate("release_date").toLocalDate())
                    .duration(resultSet.getInt("duration"))
                    .mpa(mpa)
                    .build();
        }
    }
}
