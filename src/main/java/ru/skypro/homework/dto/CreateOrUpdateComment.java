package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateOrUpdateComment {
    private String text;
    private long createdAt;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}