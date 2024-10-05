package ru.comments.commentservice.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDeletionEvent {

    @Positive
    @JsonProperty("newsId")
    private Integer newsId;

}