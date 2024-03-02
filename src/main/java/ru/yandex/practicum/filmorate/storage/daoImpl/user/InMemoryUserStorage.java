package ru.yandex.practicum.filmorate.storage.daoImpl.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Deprecated
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public User addUser(User newUser) {
        Validator.isValid(newUser);
        newUser.setId(idCounter++);
        userMap.put(newUser.getId(), newUser);
        log.info("В базу добавлен новый пользователь с id = " + newUser.getId());
        return newUser;
    }

    @Override
    public User updateUser(User updateUser) {
        Validator.isValid(updateUser);
        if (!userMap.containsKey(updateUser.getId())) {
            log.error("Пытаемся обновить пользователя с id = " + updateUser.getId() + ", но его нет в базе пользователей");
            throw new UserNotFoundException("Пользователя нет в базе пользователей");
        }
        userMap.put(updateUser.getId(), updateUser);
        log.info("Данные пользователя с id = " + updateUser.getId() + " обновлены в базе пользователей");
        return updateUser;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получаем данные обо всех пользователях из базы");
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserById(Long userId) {
        if (!userMap.containsKey(userId)) {
            log.error("Пытаемся получить данные о пользователе по id = " + userId + ", но его нет в базе пользователей");
            throw new UserNotFoundException("Фильм не найден в базе фильмов");
        }
        log.info("Получаем данные о пользователе по id = " + userId + " из базы пользователей");
        return userMap.get(userId);
    }

    @Override
    public List<User> getUsersById(List<Long> ids) {
        throw new AssertionError("Функциональность недоступна");
    }
}
