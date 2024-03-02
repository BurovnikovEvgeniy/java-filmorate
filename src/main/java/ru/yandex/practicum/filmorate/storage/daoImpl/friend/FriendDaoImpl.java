package ru.yandex.practicum.filmorate.storage.daoImpl.friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AddNewDataInDBException;
import ru.yandex.practicum.filmorate.exceptions.DeleteDataInDBException;
import ru.yandex.practicum.filmorate.exceptions.RelationshipNotFoundException;
import ru.yandex.practicum.filmorate.storage.dao.FriendDao;

import java.util.List;

@Component
@Slf4j
public class FriendDaoImpl implements FriendDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriendRelationships(long userId, long friendId, boolean status) {
        log.info("Добавляем информацию в базу о дружбе пользователя с id =" + friendId + " и id = " + userId + " со статусом " + status);
        try {
            jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, status_confirm) "
                    + "VALUES(?, ?, ?)", userId, friendId, status);
            if (status) {
                log.info("Подтверждаем заявку в друзья");
                jdbcTemplate.update(" UPDATE friends SET status_confirm=? WHERE friendId=?", status, friendId);
            }
        } catch (DataAccessException e) {
            log.error("Операция добавления в друзья завершилась неудачно");
            throw new AddNewDataInDBException("Не смогли создать в бд дружескую связь между userId = " + userId + " и friendId = " + friendId);
        }
    }

    @Override
    public void deleteAllFriends(long userId) {
        log.info("Удаляем информацию обо всех отношениях для пользователя с id = " + userId + " в базе");
        try {
            jdbcTemplate.update("DELETE FROM friends WHERE user_id=? OR friend_id=?", userId, userId);
        } catch (DataAccessException e) {
            log.error("Операция удаления всех друзей пользователя завершилась неудачно");
            throw new DeleteDataInDBException("Не удалось удалить все дружеские связи между userId = " + userId);
        }
    }

    @Override
    public void deleteFriends(long userId, long notFriendId) {
        log.info("Раздруживаем пользователей с id = " + userId + " и с id = " + notFriendId);
        try {
            jdbcTemplate.update("DELETE FROM friends WHERE user_id=? "
                    + "AND friend_id=?", userId, notFriendId);
            jdbcTemplate.update("DELETE FROM friends WHERE user_id=? "
                    + "AND friend_id=?", notFriendId, userId);
        } catch (DataAccessException e) {
            log.error("Операция удаления отношений между пользователями завершилась неудачно");
            throw new DeleteDataInDBException("Не удалось удалить дружескую связь между userId = " + userId + " и friendId = " + notFriendId);
        }
    }

    @Override
    public List<Long> getFriendsByUserId(long userId) {
        log.info("Получаем данные в базе обо всех друзьях пользователя с id = " + userId);
        try {
            List<Long> friends = jdbcTemplate.queryForList(
                    "SELECT u.user_id FROM users AS u INNER JOIN friends AS f on u.user_id = f.friend_id WHERE f.user_id = ?",
                    Long.TYPE,
                    userId
            );
            if (friends.isEmpty()) {
                log.warn("У пользователя с id = " + userId + " не найдено друзей");
            }
            return friends;
        } catch (DataAccessException e) {
            log.error("Операция получения всех друзей пользорвателя завершилась неуспешно");
            throw new RelationshipNotFoundException("Не удалось получить всех друзей пользователя с id = " + userId);
        }
    }
}