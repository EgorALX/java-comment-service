package ru.comments.commentservice.service;

import org.springframework.data.domain.PageRequest;
import ru.comments.commentservice.model.CommentDto;
import ru.comments.commentservice.model.NewCommentDto;
import ru.comments.commentservice.model.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getComments(Long newsId, PageRequest pageRequest);

    CommentDto getById(Long commentId);

    CommentDto add(NewCommentDto dto);

    void createComment(NewCommentDto commentDto);

    void deleteCommentsByNewsId(Long newsId);

    CommentDto update(Long commentId, UpdateCommentDto dto);

    void removeById(Long commentId);
}
