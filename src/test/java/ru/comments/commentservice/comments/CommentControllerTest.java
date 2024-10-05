package ru.comments.commentservice.comments;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.comments.commentservice.controller.CommentController;
import ru.comments.commentservice.dto.NewCommentDto;
import ru.comments.commentservice.dto.UpdateCommentDto;
import ru.comments.commentservice.dto.CommentDto;
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

    private final CommentDto commentDto = new CommentDto(1, 1, "desc");

    private final NewCommentDto newCommentDto = new NewCommentDto(1, 1, "desc");

    private final UpdateCommentDto updateCommentDto = new UpdateCommentDto(2, 2, "newDesc");

    private final CommentDto updatedCommentDto = new CommentDto(2, 2, "newDesc");

    @Test
    @SneakyThrows
    void addCommentTest() {
        when(commentService.add(any(NewCommentDto.class))).thenReturn(commentDto);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(commentDto.getUserId()), Integer.class))
                .andExpect(jsonPath("$.news_id", is(commentDto.getNewsId())))
                .andExpect(jsonPath("$.description", is(commentDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void updateCommentTest() {
        when(commentService.update(any(Integer.class), any(UpdateCommentDto.class))).thenReturn(updatedCommentDto);

        mockMvc.perform(patch("/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommentDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(updatedCommentDto.getUserId()), Integer.class))
                .andExpect(jsonPath("$.news_id", is(updatedCommentDto.getNewsId())))
                .andExpect(jsonPath("$.description", is(updatedCommentDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void deleteCommentTest() {
        mockMvc.perform(delete("/comments/1")).andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void getByIdTest() {
        when(commentService.getById(any(Integer.class))).thenReturn(commentDto);

        mockMvc.perform(get("/comments/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(commentDto.getUserId()), Integer.class))
                .andExpect(jsonPath("$.news_id", is(commentDto.getNewsId())))
                .andExpect(jsonPath("$.description", is(commentDto.getDescription())));
    }

    @SneakyThrows
    @Test
    void getCommentsTest() {
        int page = 1;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        when(commentService.getComments(any(Integer.class), any(PageRequest.class)))
                .thenReturn(List.of(commentDto));

        mockMvc.perform(get("/comments")
                        .param("news_Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].news_id", is(commentDto.getNewsId()), Integer.class))
                .andExpect(jsonPath("$.[0].user_id", is(commentDto.getUserId())))
                .andExpect(jsonPath("$.[0].description", is(commentDto.getDescription())));
    }
}
