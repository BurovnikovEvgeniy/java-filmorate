package ru.yandex.practicum.filmorate.storage.daoImpl.rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(long id) {
        log.info("Получаем данные о рейтинге с id = " + id + " из базы");
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM rating_type WHERE rating_id=?", new RatingMapper(), id);
        } catch (DataAccessException e) {
            throw new MpaNotFoundException("Не удалось обновить лайки у фильма с id = " + id);
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        log.info("Получаем данные обо всех рейтингах из базы");
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM rating_type ORDER BY rating_id", new RatingMapper()));
    }

    static class RatingMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Mpa mpa = new Mpa();
            mpa.setId(resultSet.getLong("rating_id"));
            mpa.setName(resultSet.getString("name"));
            return mpa;
        }
    }
}
