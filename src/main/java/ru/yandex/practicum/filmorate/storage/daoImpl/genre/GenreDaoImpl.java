package ru.yandex.practicum.filmorate.storage.daoImpl.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(long genreId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM genre_type WHERE genre_id=?", new GenreMapper(), genreId);
        } catch (DataAccessException e) {
            throw new GenreNotFoundException("Не найден жанр с id = " + genreId);
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM genre_type", new GenreMapper()));
    }


    static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        }
    }
}
