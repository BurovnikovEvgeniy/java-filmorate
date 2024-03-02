package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film newFilm);

    Film updateFilm(Film updateFilm);

    List<Film> getAllFilms();

    Film getFilmById(Long filmId);
}
