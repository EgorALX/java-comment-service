package ru.comments.commentservice.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.comments.commentservice.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByNewsId(Integer newsId, PageRequest pageRequest);

    void deleteAllByNewsId(Integer newsId);
}
