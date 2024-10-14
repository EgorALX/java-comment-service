package ru.comments.commentservice.comments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.comments.commentservice.model.Comment;
import ru.comments.commentservice.model.CommentDto;
import ru.comments.commentservice.model.NewCommentDto;
import ru.comments.commentservice.mapper.CommentMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        commentMapper = Mappers.getMapper(CommentMapper.class);
    }

    @Test
    void toCommentTest() {
        NewCommentDto newCommentDto = new NewCommentDto();
        newCommentDto.setUserId(1L);
        newCommentDto.setNewsId(2L);
        newCommentDto.setDescription("description");
        Comment comment = commentMapper.toComment(newCommentDto);
        assertEquals(comment.getUserId(), newCommentDto.getUserId());
        assertEquals(comment.getNewsId(), newCommentDto.getNewsId());
        assertEquals(comment.getDescription(), newCommentDto.getDescription());
    }
    @Test
    void toDtoTest() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUserId(2L);
        comment.setNewsId(3L);
        comment.setDescription("description");
        CommentDto commentDto = commentMapper.toDto(comment);
        assertEquals(comment.getUserId(), commentDto.getUserId());
        assertEquals(comment.getNewsId(), commentDto.getNewsId());
        assertEquals(comment.getDescription(), commentDto.getDescription());
    }


}
