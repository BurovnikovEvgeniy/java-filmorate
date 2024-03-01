package ru.yandex.practicum.filmorate.exceptions;

public class RelationshipNotFoundException extends RuntimeException {
    public RelationshipNotFoundException(String message) {
        super(message);
    }
}
