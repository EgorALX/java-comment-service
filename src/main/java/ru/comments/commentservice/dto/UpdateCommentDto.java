package ru.comments.commentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("news_id")
    private Integer newsId;

    @Size(max = 256)
    private String description;

}
