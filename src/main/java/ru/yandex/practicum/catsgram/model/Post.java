package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

// При этом аннотируйте каждый из классов аннотацией @Data
@Data
// Доработайте классы Post и Image. Укажите, что в реализации методов equals и hashCode должно участвовать только поле id.
@EqualsAndHashCode(of = { "id" })
public class Post {
    private Long id; // уникальный идентификатор сообщения
    private Long authorId; // пользователь, который создал сообщение
    private String description; // текстовое описание сообщения
    private Instant postDate; // дата и время создания сообщения
}
