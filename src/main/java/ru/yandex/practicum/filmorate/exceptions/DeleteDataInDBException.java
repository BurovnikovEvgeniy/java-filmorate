package ru.yandex.practicum.filmorate.exceptions;

public class DeleteDataInDBException extends RuntimeException {
    public DeleteDataInDBException(String message) {
        super(message);
    }
}
