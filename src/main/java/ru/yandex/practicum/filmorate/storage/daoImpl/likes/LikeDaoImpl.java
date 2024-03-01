package ru.yandex.practicum.filmorate.storage.daoImpl.likes;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserLikeNotFoundException;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addLike(long filmId, long userId) {
        try {
            if (!getLikesByFilmId(filmId).contains(userId)) {
                jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
            }
        } catch (DataAccessException e) {
            throw new UserLikeNotFoundException("Пользователю с id = " + userId + " не удалось установить лайк для фильма с id = " + filmId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        try {
            jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", filmId, userId);
        } catch (DataAccessException e) {
            throw new UserLikeNotFoundException("Пользователю с id = " + userId + " не удалось удплить лайк для фильма с id = " + filmId);
        }
    }

    @Override
    public List<Long> getLikesByFilmId(long filmId) {
        log.info("Запрашиваем лайки для фильма с id = " + filmId);
        return new ArrayList<>(jdbcTemplate.queryForList("SELECT user_id FROM likes WHERE film_id=?", Long.TYPE, filmId));
    }

    @Override
    public void addLikesForFilmId(long filmId, Set<Long> users) {
        if (users != null) {
            final ArrayList<Long> usersList = new ArrayList<>(users);
            try {
                jdbcTemplate.batchUpdate(
                        "INSERT INTO likes (film_id, user_id) VALUES (?, ?)",
                        new BatchPreparedStatementSetter() {
                            public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                                ps.setLong(1, filmId);
                                ps.setLong(2, usersList.get(i));
                            }

                            public int getBatchSize() {
                                return usersList.size();
                            }
                        }
                );
            } catch (DataAccessException e) {
                throw new GenreNotFoundException("Не удалось добавить лайки для фильма с id = " + filmId);
            }
        }
    }

    @Override
    public void updateLikesByFilmId(long filmId, Set<Long> users) {
        try {
            jdbcTemplate.update("DELETE FROM likes WHERE film_id=?", filmId);
            addLikesForFilmId(filmId, users);
        } catch (DataAccessException e) {
            throw new GenreNotFoundException("Не удалось обновить лайки у фильма с id = " + filmId);
        }
    }

}
