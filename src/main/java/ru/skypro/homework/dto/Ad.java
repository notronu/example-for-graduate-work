package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class Ad {
    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
