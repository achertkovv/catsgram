package ru.yandex.practicum.catsgram.model;

// Для удобства работы с параметром сортировки, можно добавить в код перечисление, которое будет
// проверять строку, переданную в качестве параметра запроса, и возвращать правильный элемент перечисления.
// Его затем нужно передать в метод findAll сервиса PostService вместе с остальными данными.
public enum SortOrder {
    ASCENDING, DESCENDING;

    // Преобразует строку в элемент перечисления
    public static SortOrder from(String order) {
        return switch (order.toLowerCase()) {
            case "ascending", "asc" -> ASCENDING;
            case "descending", "desc" -> DESCENDING;
            default -> null;
        };
    }
}