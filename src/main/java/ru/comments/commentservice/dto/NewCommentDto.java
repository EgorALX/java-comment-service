package ru.comments.commentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {

    @NotNull
    @Positive
    @JsonProperty("user_id")
    private Integer userId;

    @NotNull
    @Positive
    @JsonProperty("news_id")
    private Integer newsId;

    @NotBlank
    @Size(max = 256)
    private String description;

}
