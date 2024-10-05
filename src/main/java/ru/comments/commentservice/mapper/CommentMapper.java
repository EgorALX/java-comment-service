package ru.comments.commentservice.mapper;

import org.springframework.stereotype.Component;
import ru.comments.commentservice.dto.CommentDto;
import ru.comments.commentservice.dto.NewCommentDto;
import ru.comments.commentservice.model.Comment;

@Component
public class CommentMapper {

    public Comment toComment(NewCommentDto dto) {
        return new Comment(0, dto.getUserId(), dto.getNewsId(), dto.getDescription());
    }

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getUserId(), comment.getNewsId(), comment.getDescription());
    }
}
