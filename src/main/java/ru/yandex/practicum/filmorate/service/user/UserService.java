package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendDao;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FriendDao friendDao;

    @Autowired
    public UserService(UserStorage userStorage, FriendDao friendDao) {
        this.userStorage = userStorage;
        this.friendDao = friendDao;
    }

    public User createUser(User user) {
        log.info("Пытаемся добавить нового пользователя");
        return userStorage.addUser(user);
    }

    public User updateUser(User updateUser) {
        log.info("Пытаемся обновить данные о пользователе");
        friendDao.deleteAllFriends(updateUser.getId());
        for (Long friendId : updateUser.getFriends()) {
            boolean status = userStorage.getUserById(friendId).getFriends().contains(updateUser.getId());
            friendDao.addFriendRelationships(updateUser.getId(), friendId, status);
        }
        return userStorage.updateUser(updateUser);
    }

    public List<User> getUsers() {
        log.info("Пытаемся получить все данные о пользователях");
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        log.info("Пытаемся получить пользователя по id");
        return userStorage.getUserById(id);
    }

    public void addFriend(long userId, long friendId) {
        log.info("Пытаемся передружить пользователей с id = " + userId + " и с id = " + friendId);
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        boolean status = friendDao.getFriendsByUserId(userId).contains(friendId);
        friendDao.addFriendRelationships(userId, friendId, status);
    }

    public void deleteFriend(long userId, long notFriendId) {
        log.info("Пользователь с id = " + userId + " пытается удалить из друзей пользователя с id = " + notFriendId);
        friendDao.deleteFriends(userId, notFriendId);
    }

    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        log.info("Пытаемся узнать общих друзей пользователя с id = " + firstUserId + " и с id = " + secondUserId);
        List<Long> firstUserFriends = friendDao.getFriendsByUserId(firstUserId);
        List<Long> secondUserFriends = friendDao.getFriendsByUserId(secondUserId);
        List<User> commonFriends = new ArrayList<>();
        for (Long currentFriendCandidate: firstUserFriends) {
            if (secondUserFriends.contains(currentFriendCandidate)) {
                commonFriends.add(userStorage.getUserById(currentFriendCandidate));
            }
        }
        return commonFriends;
    }

    public List<User> getFriendsById(Long userId) {
        log.info("Пытаемся узнать список всех друзей пользователя с id = " + userId);
        return friendDao.getFriendsByUserId(userId)
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
