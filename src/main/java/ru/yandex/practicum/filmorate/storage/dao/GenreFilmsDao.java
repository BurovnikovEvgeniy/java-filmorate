package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreFilmsDao {

    List<Genre> getGenresByFilmId(long filmId);

    void addGenresForFilmId(long filmId, List<Genre> genres);

    void updateGenresByFilmId(long filmId, List<Genre> genres);
}
