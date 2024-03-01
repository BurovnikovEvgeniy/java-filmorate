package ru.yandex.practicum.filmorate.service.rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.List;

@Service
@Slf4j
public class RatingService {

    private final MpaDao mpaDao;

    @Autowired
    public RatingService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getAllRating() {
        log.info("Пытаемся получить список всех рейтингов");
        return mpaDao.getAllMpa();
    }

    public Mpa getRatingId(@PathVariable int id) {
        log.info("Пытаемся получить данные о рейтинге c id = " + id);
        return mpaDao.getMpaById(id);
    }
}
