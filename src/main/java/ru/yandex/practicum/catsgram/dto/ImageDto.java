package ru.yandex.practicum.catsgram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String originalFileName;
}
