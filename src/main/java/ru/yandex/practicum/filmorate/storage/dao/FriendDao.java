package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;

public interface FriendDao {

    void addFriendRelationships(long userId, long friendId, boolean status);

    void deleteFriends(long userId, long notFriendId);

    List<Long> getFriendsByUserId(long userId);

    void deleteAllFriends(long userId);
}
