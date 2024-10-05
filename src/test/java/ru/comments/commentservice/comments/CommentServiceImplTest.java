package ru.comments.commentservice.comments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import ru.comments.commentservice.model.CommentDto;
import ru.comments.commentservice.model.NewCommentDto;
import ru.comments.commentservice.model.UpdateCommentDto;
import ru.comments.commentservice.mapper.CommentMapper;
import ru.comments.commentservice.model.Comment;
import ru.comments.commentservice.repository.CommentRepository;
import ru.comments.commentservice.controller.exception.model.NotFoundException;
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
                new Comment(1L, 1L, 1L, "desc"),
                new Comment(2L, 2L, 2L, "another desc")
        );
        comment = new Comment(1L, 1L, 1L, "desc");
        commentDto = new CommentDto();
        commentDto.setNewsId(1L);
        commentDto.setUserId(1L);
        commentDto.setDescription("desc");
        newCommentDto = new NewCommentDto();
        newCommentDto.setNewsId(1L);
        newCommentDto.setUserId(1L);
        newCommentDto.setDescription("desc");
        updateCommentDto = new UpdateCommentDto();
        updateCommentDto.setNewsId(1L);
        updateCommentDto.setUserId(1L);
        updateCommentDto.setDescription("updated desc");
    }

    @Test
    void getCommentsTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(commentRepository.findAllByNewsId(any(Long.class), eq(pageRequest))).thenReturn(comments);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        List<CommentDto> result = commentService.getComments(1L, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(commentDto.getUserId(), result.get(0).getUserId());
        assertEquals(commentDto.getNewsId(), result.get(0).getNewsId());
        assertEquals(commentDto.getDescription(), result.get(0).getDescription());
    }


    @Test
    void getByIdTest() {
        when(commentRepository.findById(eq(1L))).thenReturn(java.util.Optional.of(comment));
        when(commentMapper.toDto(eq(comment))).thenReturn(commentDto);

        CommentDto result = commentService.getById(1L);

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
        commentService.deleteCommentsByNewsId(1L);

    }

    @Test
    void removeByIdTest() {
        commentService.removeById(1L);

    }

    @Test
    void getByIdWithIncorrectIdTest() {
        when(commentRepository.findById(eq(999L))).thenThrow(new NotFoundException("Comment not found"));

        assertThrows(NotFoundException.class, () -> commentService.getById(999L));
    }

    @Test
    void updateWithIncorrectIdTest() {
        when(commentRepository.findById(eq(999L))).thenThrow(new NotFoundException("Comment not found"));

        assertThrows(NotFoundException.class, () -> commentService.update(999L, updateCommentDto));
    }

    @Test
    void createCommentFromKafkaTopicTest() {
        when(commentMapper.toComment(any(NewCommentDto.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.createComment(newCommentDto);

    }
}