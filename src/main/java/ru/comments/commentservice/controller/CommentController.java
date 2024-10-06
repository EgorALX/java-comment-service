package ru.comments.commentservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.comments.commentservice.model.CommentDto;
import ru.comments.commentservice.model.NewCommentDto;
import ru.comments.commentservice.model.UpdateCommentDto;
import ru.comments.commentservice.model.ParamsUserDto;
import ru.comments.commentservice.service.CommentService;

import java.util.List;

@Slf4j
@RequestMapping("/comments")
@RestController
@Validated
@RequiredArgsConstructor
public class CommentController implements CommentsApi {

    private final CommentService commentService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDto> createComment(@RequestBody NewCommentDto newCommentDto) {
        log.info("Starting createComment method. Creating comment: {}", newCommentDto.toString());
        CommentDto comment = commentService.add(newCommentDto);
        log.info("Completed createComment method successfully. Result: {}", comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @Override
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long commentId) {
        log.info("Starting getCommentById method. Getting comment by commentId={}", commentId);
        CommentDto comment = commentService.getById(commentId);
        log.info("Completed getCommentById method successfully. Result: {}", comment);
        return ResponseEntity.ok(comment);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@ParameterObject ParamsUserDto queryParams) {
        log.info("Starting getComments method. Getting comments with params: {}", queryParams);
        PageRequest pageRequest = PageRequest.of(queryParams.getPage(), queryParams.getSize());
        List<CommentDto> comments = commentService.getComments(queryParams.getNewsId(), pageRequest);
        log.info("Completed getComments method successfully. Results count: {}", comments.size());
        return ResponseEntity.ok(comments);
    }

    @Override
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> removeComment(@PathVariable Long commentId) {
        log.info("Starting removeComment method. Removing comment with commentId={}", commentId);
        boolean isRemoved = commentService.removeById(commentId);
        if (isRemoved) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
            log.info("Completed removeComment method successfully");
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId,
                                                    @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Starting updateComment method. Updating comment with commentId={}", commentId);
        CommentDto comment = commentService.update(commentId, updateCommentDto);
        log.info("Completed updateComment method successfully. Result: {}", comment);
        return ResponseEntity.ok(comment);
    }
}
