package ru.skypro.homework.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.AdEntity;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 64)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private AdEntity ad;
}