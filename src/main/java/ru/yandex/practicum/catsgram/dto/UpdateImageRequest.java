package ru.yandex.practicum.catsgram.dto;

import lombok.Data;

@Data
public class UpdateImageRequest {
    private Long postId;
    private String originalFileName;
    private String filePath;

    public boolean hasPostId() {
        return !(postId == null);
    }

    public boolean hasOriginalFileName() {
        return !(originalFileName == null || originalFileName.isBlank());
    }

    public boolean hasFilePath() {
        return !(filePath == null || filePath.isBlank());
    }
}
