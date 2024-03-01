package ru.yandex.practicum.filmorate.storage.daoImpl.genre;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreFilmsDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GenreFilmsImpl implements GenreFilmsDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreFilmsImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        return new ArrayList<>(jdbcTemplate.query(
                "SELECT gt.genre_id, name FROM genre_type AS gt JOIN genre_films AS gf ON gt.genre_id = gf.genre_id " +
                        "WHERE gf.film_id=? ORDER BY gt.genre_id ASC",
                new GenreDaoImpl.GenreMapper(),
                filmId)
        );
    }

    @Override
    public void addGenresForFilmId(long filmId, List<Genre> genres) {
        if (genres != null) {
            final ArrayList<Genre> genreList = new ArrayList<>(genres);
            try {
                jdbcTemplate.batchUpdate(
                        "INSERT INTO genre_films (film_id, genre_id) VALUES (?, ?)",
                        new BatchPreparedStatementSetter() {
                            public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                                ps.setLong(1, filmId);
                                ps.setLong(2, genreList.get(i).getId());
                            }

                            public int getBatchSize() {
                                return genreList.size();
                            }
                        }
                );
            } catch (DuplicateKeyException e) {
                log.error("Жанры у фильма не должны дублироваться");
            }
            catch (DataAccessException e) {
                throw new GenreNotFoundException("");
            }
        }
    }

    @Override
    public void updateGenresByFilmId(long filmId, List<Genre> genres) {
        try {
            jdbcTemplate.update("DELETE FROM genre_films WHERE film_id=?", filmId);
            addGenresForFilmId(filmId, genres);
        } catch (DataAccessException e) {
            throw new GenreNotFoundException("Не удалось обновить жанры у фильма с id = " + filmId);
        }
    }
}
