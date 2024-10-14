package ru.comments.commentservice.comments;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.comments.commentservice.controller.CommentController;
import ru.comments.commentservice.model.CommentDto;
import ru.comments.commentservice.model.NewCommentDto;
import ru.comments.commentservice.model.UpdateCommentDto;
import ru.comments.commentservice.service.CommentService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private CommentDto commentDto;

    private NewCommentDto newCommentDto;

    private UpdateCommentDto updateCommentDto;

    private CommentDto updatedCommentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        updatedCommentDto = new CommentDto();
        updatedCommentDto.setNewsId(1L);
        updatedCommentDto.setUserId(1L);
        updatedCommentDto.setDescription("updated desc");
    }

    @Test
    @SneakyThrows
    void addCommentTest() {
        when(commentService.add(any(NewCommentDto.class))).thenReturn(commentDto);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(commentDto.getUserId()), Long.class))
                .andExpect(jsonPath("$.news_id", is(commentDto.getNewsId()), Long.class))
                .andExpect(jsonPath("$.description", is(commentDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void updateCommentTest() {
        when(commentService.update(any(Long.class), any(UpdateCommentDto.class))).thenReturn(updatedCommentDto);

        mockMvc.perform(patch("/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommentDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(commentDto.getUserId()), Long.class))
                .andExpect(jsonPath("$.news_id", is(commentDto.getNewsId()), Long.class))
                .andExpect(jsonPath("$.description", is(updatedCommentDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void deleteCommentTest() {
        mockMvc.perform(delete("/comments/1")).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getByIdTest() {
        when(commentService.getById(any(Long.class))).thenReturn(commentDto);

        mockMvc.perform(get("/comments/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(commentDto.getUserId()), Long.class))
                .andExpect(jsonPath("$.news_id", is(commentDto.getNewsId()), Long.class))
                .andExpect(jsonPath("$.description", is(commentDto.getDescription())));
    }

    @SneakyThrows
    @Test
    void getCommentsTest() {
        when(commentService.getComments(any(), any()))
                .thenReturn(List.of(commentDto));

        mockMvc.perform(get("/comments")
                        .param("newsId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].news_id", is(commentDto.getNewsId()), Long.class))
                .andExpect(jsonPath("$.[0].user_id", is(commentDto.getUserId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(commentDto.getDescription())));
    }
}
