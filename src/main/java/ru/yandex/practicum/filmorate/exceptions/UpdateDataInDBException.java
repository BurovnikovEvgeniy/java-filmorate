package ru.yandex.practicum.filmorate.exceptions;

public class UpdateDataInDBException extends RuntimeException {
    public UpdateDataInDBException(String message) {
        super(message);
    }
}
