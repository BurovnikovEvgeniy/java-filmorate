package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Long, User> userMap = new HashMap<>();
    private static int idCounter = 0;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        Validator.isValid(user);
        user.setId(idCounter++);
        userMap.put(user.getId(), user);
        LOGGER.info("Добавлен новый пользователь с id = " + user.getId());
        return user;
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    public User updateUser(@RequestBody User updateUser) {
        Validator.isValid(updateUser);
        if (!userMap.containsKey(updateUser.getId())) {
            LOGGER.error("Пытаемся обновить пользователя с id = " + updateUser.getId() + ", но его нет в базе пользователей");
            throw new UserNotFoundException("Пользователя нет в базе пользователей");
        }
        userMap.put(updateUser.getId(), updateUser);
        LOGGER.info("Данные пользователя с id = " + updateUser.getId() + " обновлены");
        return updateUser;
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    public List<User> getUsers() {
        LOGGER.info("Получаем данные всех пользователей");
        return new ArrayList<>(userMap.values());
    }
}
