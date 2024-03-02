package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User newUser);

    User updateUser(User updateUser);

    List<User> getAllUsers();

    User getUserById(Long userId);

    List<User> getUsersById(List<Long> ids);
}
