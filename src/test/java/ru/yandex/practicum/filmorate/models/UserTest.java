package ru.yandex.practicum.filmorate.models;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.BaseTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest extends BaseTest {

    @Test
    public void createValidUser() {
        User user = User.builder()
                .id(1L)
                .email("user@mail.ru")
                .login("user")
                .name("Evgeniy")
                .birthday(LocalDate.of(1991, 12,1))
                .build();
        assertDoesNotThrow(() -> Validator.isValid(user));
    }

    @Test
    public void createUserWithIncorrectEmail() {
        User user = User.builder()
                .id(1L)
                .email("usermail.ru")
                .login("user")
                .name("Evgeniy")
                .birthday(LocalDate.of(1991, 12,1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.isValid(user));
        assertEquals("Электронная почта невалидна", exception.getMessage());
    }

    @Test
    public void createUserWithEmptyEmail() {
        User user = User.builder()
                .id(1L)
                .email("")
                .login("user")
                .name("Evgeniy")
                .birthday(LocalDate.of(1991, 12,1))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.isValid(user));
        assertEquals("Электронная почта невалидна", exception.getMessage());
    }

    @Test
    public void createUserWithEmptyLogin() {
        User user = User.builder()
                .id(1L)
                .email("user@mail.ru")
                .login("")
                .name("Evgeniy")
                .birthday(LocalDate.of(1991, 12,1))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.isValid(user));
        assertEquals("Логин невалиден", exception.getMessage());
    }


    @Test
    public void createUserWithEmptyName() {
        User user = User.builder()
                .id(1L)
                .email("user@mail.ru")
                .login("user")
                .birthday(LocalDate.of(1991, 12,1))
                .build();
        assertDoesNotThrow(() -> Validator.isValid(user));
        assertEquals(user.getLogin(), user.getName(), "Значение имени отличается от логина");
    }

    @Test
    public void createUserWithIncorrectBirthday() {
        User user = User.builder()
                .id(1L)
                .email("user@mail.ru")
                .login("user")
                .name("Evgeniy")
                .birthday(LocalDate.of(2191, 12,1))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> Validator.isValid(user));
        assertEquals("Дата рождения невалидна", exception.getMessage());
    }
}
