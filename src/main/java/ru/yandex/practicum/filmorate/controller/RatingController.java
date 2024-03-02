package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import java.util.List;

@Slf4j
@RestController
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllRating() {
        log.info("Поступил запрос на получение списка всех рейтингов");
        return ratingService.getAllRating();
    }

    @GetMapping("mpa/{id}")
    public Mpa getRatingId(@PathVariable int id) {
        log.info("Поступил запрос на получение рейтинга с id " + id);
        return ratingService.getRatingId(id);
    }
}
