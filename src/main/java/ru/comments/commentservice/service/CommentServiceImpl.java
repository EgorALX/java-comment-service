package ru.comments.commentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.comments.commentservice.model.CommentDto;
import ru.comments.commentservice.model.NewCommentDto;
import ru.comments.commentservice.model.UpdateCommentDto;
import ru.comments.commentservice.controller.exception.model.NotFoundException;
import ru.comments.commentservice.mapper.CommentMapper;
import ru.comments.commentservice.model.Comment;
import ru.comments.commentservice.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;
    private static final String COMMENT_NOT_FOUND = "Comment not found";

    private KafkaTemplate<String, NewCommentDto> kafkaTemplate;

    @Override
    public List<CommentDto> getComments(Long newsId, PageRequest pageRequest) {
        List<Comment> comments = commentRepository.findAllByNewsId(newsId, pageRequest);
        if (comments.isEmpty()) throw new NotFoundException(COMMENT_NOT_FOUND);
        return comments.stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getById(Long commentId) {
        Comment comment = commentRepository
                .findById(commentId).orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto add(NewCommentDto dto) {
        Comment comment = commentMapper.toComment(dto);
        Comment newComment = commentRepository.save(comment);
        return commentMapper.toDto(newComment);
    }

    @Override
    @KafkaListener(topics = "create-comment-topic", containerFactory = "createCommentKafkaListenerContainerFactory")
    public void createComment(NewCommentDto commentDto) {
        Comment comment = commentMapper.toComment(commentDto);
        commentRepository.save(comment);
    }

    @Override
    public void deleteCommentsByNewsId(Long newsId) {
        commentRepository.deleteAllByNewsId(newsId);
    }

    @Override
    public CommentDto update(Long commentId, UpdateCommentDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));

        Optional.ofNullable(dto.getUserId()).ifPresent(comment::setUserId);
        Optional.ofNullable(dto.getNewsId()).ifPresent(comment::setNewsId);
        Optional.ofNullable(dto.getDescription()).ifPresent(comment::setDescription);

        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public boolean removeById(Long commentId) {
        try {
            commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
            commentRepository.deleteById(commentId);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
}