package ru.yandex.practicum.filmorate.exceptions;

public class UserLikeNotFoundException extends RuntimeException {
    public UserLikeNotFoundException(String message) {
        super(message);
    }
}
