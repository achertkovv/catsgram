package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

// При этом аннотируйте каждый из классов аннотацией @Data
@Data
// Доработайте классы Post и Image. Укажите, что в реализации методов equals и hashCode должно участвовать только поле id.
@EqualsAndHashCode(of = { "id" })
public class Image {
    private Long id; //  уникальный идентификатор изображения
    private long postId; // уникальный идентификатор поста, к которому прикреплено изображение
    private String originalFileName; // имя файла, который содержит изображение
    private String filePath; // путь, по которому изображение было сохранено
}
