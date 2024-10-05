package ru.comments.commentservice.consumer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.comments.commentservice.dto.NewCommentDto;
import ru.comments.commentservice.dto.NewsDeletionEvent;
import ru.comments.commentservice.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Validated
@EnableKafka
@Slf4j
public class KafkaConsumer {

    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "create-comment-topic", groupId = "comment-create-group")
    public void consume(@Valid NewCommentDto request) {
        log.debug("Received new comment request: {}", request);
        commentService.createComment(request);
    }

    @KafkaListener(topics = "news-deletion-topic", groupId = "news-delete-group")
    public void deleteNews(String message) {
        log.debug("Received news deletion message: {}", message);
        try {
            System.out.println(message);
            NewsDeletionEvent deletionEvent = objectMapper.readValue(message, NewsDeletionEvent.class);
            Integer id = deletionEvent.getNewsId();
            log.info("Deleting comments for news ID: {}", id);
            commentService.deleteCommentsByNewsId(id);
            log.info("Successfully deleted comments for news ID: {}", id);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON message for news deletion", e);
        } catch (Exception e) {
            log.error("Error processing news deletion event", e);
        }
    }
}