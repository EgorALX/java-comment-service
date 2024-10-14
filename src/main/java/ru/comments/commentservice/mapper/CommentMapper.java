package ru.comments.commentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.comments.commentservice.model.Comment;
import ru.comments.commentservice.model.CommentDto;
import ru.comments.commentservice.model.NewCommentDto;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    Comment toComment(NewCommentDto dto);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "newsId", source = "newsId")
    @Mapping(target = "description", source = "description")
    CommentDto toDto(Comment comment);

}
