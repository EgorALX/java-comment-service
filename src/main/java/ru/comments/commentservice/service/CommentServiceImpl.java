package ru.comments.commentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.comments.commentservice.dto.CommentDto;
import ru.comments.commentservice.dto.NewCommentDto;
import ru.comments.commentservice.dto.UpdateCommentDto;
import ru.comments.commentservice.exception.model.NotFoundException;
import ru.comments.commentservice.mapper.CommentMapper;
import ru.comments.commentservice.model.Comment;
import ru.comments.commentservice.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private KafkaTemplate<String, NewCommentDto> kafkaTemplate;

    @Override
    public List<CommentDto> getComments(Integer newsId, PageRequest pageRequest) {
        List<Comment> comments = commentRepository.findAllByNewsId(newsId, pageRequest);
        return comments.stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getById(Integer commentId) {
        Comment comment = commentRepository
                .findById(commentId).orElseThrow(() -> new NotFoundException("Comment " + commentId + " not found"));
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
        Integer newsId = commentDto.getNewsId();

        Comment comment = commentMapper.toComment(commentDto);
        commentRepository.save(comment);
    }

    @Override
    public void deleteCommentsByNewsId(Integer newsId) {
        commentRepository.deleteAllByNewsId(newsId);
    }

    @Override
    public CommentDto update(Integer commentId, UpdateCommentDto dto) {
        Comment comment = commentRepository
                .findById(commentId).orElseThrow(() -> new NotFoundException("Comment " + commentId + " not found"));
        if (dto.getUserId() != null) {
            comment.setUserId(dto.getUserId());
        }
        if (dto.getNewsId() != null) {
            comment.setNewsId(dto.getNewsId());
        }
        if (dto.getUserId() != null) {
            comment.setUserId(dto.getUserId());
        }
        return commentMapper.toDto(comment);
    }

    @Override
    public void removeById(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}