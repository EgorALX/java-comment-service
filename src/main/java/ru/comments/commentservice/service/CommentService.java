package ru.comments.commentservice.service;

import org.springframework.data.domain.PageRequest;
import ru.comments.commentservice.dto.CommentDto;
import ru.comments.commentservice.dto.NewCommentDto;
import ru.comments.commentservice.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getComments(Integer newsId, PageRequest pageRequest);

    CommentDto getById(Integer commentId);

    CommentDto add(NewCommentDto dto);

    void createComment(NewCommentDto commentDto);

    void deleteCommentsByNewsId(Integer newsId);

    CommentDto update(Integer commentId, UpdateCommentDto dto);

    void removeById(Integer commentId);
}
