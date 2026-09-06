package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

// При этом аннотируйте каждый из классов аннотацией @Data
@Data
// Доработайте ранее созданный класс User в проекте Catsgram. Добавьте аннотацию @EqualsAndHashCode.
// Используя элемент of, укажите, что в реализации методов equals и hashCode должно участвовать только поле email.
@EqualsAndHashCode(of = { "email" })
public class User {
    private Long id; // уникальный идентификатор пользователя
    private String username; // имя пользователя
    private String email; // электронная почта пользователя
    private String password; // пароль пользователя
    private Instant registrationDate; // дата и время регистрации
}
