package ru.comments.commentservice.comments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.comments.commentservice.dto.CommentDto;
import ru.comments.commentservice.dto.NewCommentDto;
import ru.comments.commentservice.mapper.CommentMapper;
import ru.comments.commentservice.model.Comment;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CommentMapperTest {
    @InjectMocks
    private CommentMapper commentMapper;

    @Test
    void toCommentTest() {
        NewCommentDto newCommentDto = new NewCommentDto(1, 2, "description");
        Comment comment = commentMapper.toComment(newCommentDto);
        assertEquals(comment.getUserId(), newCommentDto.getUserId());
        assertEquals(comment.getNewsId(), newCommentDto.getNewsId());
        assertEquals(comment.getDescription(), newCommentDto.getDescription());
    }
    @Test
    void toDtoTest() {
        Comment comment = new Comment(1, 2, 3, "description");
        CommentDto commentDto = commentMapper.toDto(comment);
        assertEquals(comment.getUserId(), commentDto.getUserId());
        assertEquals(comment.getNewsId(), commentDto.getNewsId());
        assertEquals(comment.getDescription(), commentDto.getDescription());
    }


}
