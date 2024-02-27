package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.logging.Logger;

@RestController
@Slf4j
@RequestMapping(value = "/users")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Поступил запрос на добавление нового пользователя");
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User updateUser) {
        log.info("Поступил запрос на обновление данных о пользователе");
        return userStorage.updateUser(updateUser);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Поступил запрос на получения всех данных о пользователях");
        return userStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        log.info("Поступил запрос на получение пользователя по id");
        return userStorage.getUserById(Long.parseLong(id));
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Поступил запрос на добавления в друзья");
        userService.addFriend(Long.parseLong(id), Long.parseLong(friendId));
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Поступил запрос на удаление из друзей");
        userService.deleteFriend(Long.parseLong(id), Long.parseLong(friendId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable String id) {
        log.info("Поступил запрос на получение данных друзей пользователя по его id");
        return userService.getFriendsById(Long.parseLong(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsByIds(@PathVariable String id, @PathVariable String otherId) {
        log.info("Поступил запрос на получение списка друзей, общих с другим пользователем");
        return userService.getCommonFriends(Long.parseLong(id), Long.parseLong(otherId));
    }
}
