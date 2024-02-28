package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) {
        log.info("Пытаемся передружить пользователей с id = " + userId + " и с id = " + friendId);
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(long userId, long notFriendId) {
        log.info("Пользователь с id = " + userId + " пытается удалить из друзей пользователя с id = " + notFriendId);
        User user = userStorage.getUserById(userId);
        User notFriend = userStorage.getUserById(notFriendId);
        user.getFriends().remove(notFriendId);
        notFriend.getFriends().remove(userId);
    }

    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        log.info("Пытаемся узнать общих друзей пользователя с id = " + firstUserId + " и с id = " + secondUserId);
        Set<Long> firstUserFriends = userStorage.getUserById(firstUserId).getFriends();
        Set<Long> secondUserFriends = userStorage.getUserById(secondUserId).getFriends();
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
        return userStorage.getUserById(userId)
                .getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
