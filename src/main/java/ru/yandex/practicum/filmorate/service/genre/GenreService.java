package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getAllGenre() {
        log.info("Получим список всех жанров");
        return genreDao.getAllGenre();
    }

    public Genre getGenreId(long  id) {
        log.info("Получим жанр с id " + id);
        return genreDao.getGenreById(id);
    }
}
