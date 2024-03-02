package ru.yandex.practicum.filmorate.storage.daoImpl.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User newUser) {
        Validator.isValid(newUser);
        jdbcTemplate.update("INSERT INTO users (email , login , name , birthday) VALUES (? , ? , ? , ?)",
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday());
        log.info("В базу добавлен новый пользователь с email = " + newUser.getEmail());
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", new UserMapper(), newUser.getEmail());
    }

    @Override
    public User updateUser(User updateUser) {
        Validator.isValid(updateUser);
        getUserById(updateUser.getId()); // check exist user
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthday=? WHERE user_id=?",
                updateUser.getEmail(),
                updateUser.getLogin(),
                updateUser.getName(),
                updateUser.getBirthday(),
                updateUser.getId()
        );
        log.info("Данные пользователя с id = " + updateUser.getId() + " обновлены в базе пользователей");
        return getUserById(updateUser.getId());
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получаем данные обо всех пользователях из базы");
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM users", new UserMapper()));
    }

    @Override
    public List<User> getUsersById(List<Long> ids) {
        log.info("Получаем данные из базы о списке пользователей");
        String inSql = String.join(",", Collections.nCopies(ids.size(), "? "));
        try {
            return jdbcTemplate.query("SELECT * FROM users WHERE users.user_id IN (" + inSql + ")", new UserMapper(), ids.toArray());
        } catch (DataAccessException e) {
            log.error("Не удалось получить данные из базы о списке пользователей " + ids.toArray());
            throw new UserNotFoundException("Ошибка получание данные о списке пользователей");
        }
    }

    @Override
    public User getUserById(Long userId) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE users.user_id=?", new UserMapper(), userId);
            log.info("Получаем данные о пользователе по id = " + userId + " из базы пользователей");
            return user;
        } catch (DataAccessException e) {
            log.error("Пытаемся получить данные о пользователе по id = " + userId + ", но его нет в базе пользователей");
            throw new UserNotFoundException("Фильм не найден в базе фильмов");
        }
    }

    static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return User.builder()
                    .id(resultSet.getInt("user_id"))
                    .email(resultSet.getString("email"))
                    .login(resultSet.getString("login"))
                    .name(resultSet.getString("name"))
                    .birthday(resultSet.getDate("birthday").toLocalDate())
                    .build();
        }
    }
}
