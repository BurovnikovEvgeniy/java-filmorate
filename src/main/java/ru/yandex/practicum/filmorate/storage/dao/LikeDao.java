package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;
import java.util.Set;

public interface LikeDao {

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Long> getLikesByFilmId(long filmId);

    void addLikesForFilmId(long filmId, Set<Long> users);

    void updateLikesByFilmId(long filmId, Set<Long> users);
}
