package ru.comments.commentservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "news_id")
    private Integer newsId;

    @Column(name = "description")
    private String description;
}
