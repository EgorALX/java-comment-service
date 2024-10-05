package ru.comments.commentservice.comments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import ru.comments.commentservice.dto.CommentDto;
import ru.comments.commentservice.dto.NewCommentDto;
import ru.comments.commentservice.dto.UpdateCommentDto;
import ru.comments.commentservice.mapper.CommentMapper;
import ru.comments.commentservice.model.Comment;
import ru.comments.commentservice.repository.CommentRepository;
import ru.comments.commentservice.exception.model.NotFoundException;
import ru.comments.commentservice.service.CommentServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private List<Comment> comments;
    private Comment comment;
    private CommentDto commentDto;
    private NewCommentDto newCommentDto;
    private UpdateCommentDto updateCommentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        comments = Arrays.asList(
                new Comment(1, 1, 1, "desc"),
                new Comment(2, 2, 2, "another desc")
        );
        comment = new Comment(1, 1, 1, "desc");
        commentDto = new CommentDto(1, 1, "desc");
        newCommentDto = new NewCommentDto(1, 1, "desc");
        updateCommentDto = new UpdateCommentDto(1, 1, "updated desc");
    }

    @Test
    void getCommentsTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(commentRepository.findAllByNewsId(any(Integer.class), eq(pageRequest))).thenReturn(comments);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        List<CommentDto> result = commentService.getComments(1, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(commentDto.getUserId(), result.get(0).getUserId());
        assertEquals(commentDto.getNewsId(), result.get(0).getNewsId());
        assertEquals(commentDto.getDescription(), result.get(0).getDescription());
    }


    @Test
    void getByIdTest() {
        when(commentRepository.findById(eq(1))).thenReturn(java.util.Optional.of(comment));
        when(commentMapper.toDto(eq(comment))).thenReturn(commentDto);

        CommentDto result = commentService.getById(1);

        assertNotNull(result);
        assertEquals(commentDto.getUserId(), result.getUserId());
        assertEquals(commentDto.getNewsId(), result.getNewsId());
        assertEquals(commentDto.getDescription(), result.getDescription());
    }

    @Test
    void addTest() {
        when(commentMapper.toComment(any(NewCommentDto.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(eq(comment))).thenReturn(commentDto);

        CommentDto result = commentService.add(newCommentDto);

        assertNotNull(result);
        assertEquals(commentDto.getUserId(), result.getUserId());
        assertEquals(commentDto.getNewsId(), result.getNewsId());
        assertEquals(commentDto.getDescription(), result.getDescription());
    }

    @Test
    void deleteCommentsByNewsIdTest() {
        commentService.deleteCommentsByNewsId(1);

    }

    @Test
    void removeByIdTest() {
        commentService.removeById(1);

    }

    @Test
    void getByIdWithIncorrectIdTest() {
        when(commentRepository.findById(eq(999))).thenThrow(new NotFoundException("Comment not found"));

        assertThrows(NotFoundException.class, () -> commentService.getById(999));
    }

    @Test
    void updateWithIncorrectIdTest() {
        when(commentRepository.findById(eq(999))).thenThrow(new NotFoundException("Comment not found"));

        assertThrows(NotFoundException.class, () -> commentService.update(999, updateCommentDto));
    }

    @Test
    void createCommentFromKafkaTopicTest() {
        when(commentMapper.toComment(any(NewCommentDto.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.createComment(newCommentDto);

    }
}