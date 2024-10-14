package ru.comments.commentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("news_id")
    private Integer newsId;

    private String description;
}
