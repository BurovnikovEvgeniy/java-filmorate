package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Like {
    private long id;
    private long userId;
    private long filmId;
}
