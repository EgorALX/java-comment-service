package ru.comments.commentservice.comments;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.comments.commentservice.model.Comment;
import ru.comments.commentservice.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void getCommentByParams() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Comment firstComment = new Comment();
        firstComment.setId(1L);
        firstComment.setUserId(1L);
        firstComment.setNewsId(1L);
        firstComment.setDescription("first description");
        Comment secondComment = new Comment();
        secondComment.setId(2L);
        secondComment.setUserId(2L);
        secondComment.setNewsId(2L);
        secondComment.setDescription("second description");
        Comment thirdComment = new Comment();
        thirdComment.setId(3L);
        thirdComment.setUserId(3L);
        thirdComment.setNewsId(3L);
        thirdComment.setDescription("third description");

        commentRepository.saveAll(List.of(firstComment, secondComment, thirdComment));
        List<Comment> commentList = commentRepository.findAllByNewsId(2L, pageRequest);

        assertNotNull(commentList);
        assertEquals(1, commentList.size());
        assertEquals(secondComment.getUserId(), commentList.get(0).getUserId());
        assertEquals(secondComment.getNewsId(), commentList.get(0).getNewsId());
        assertEquals(secondComment.getDescription(), commentList.get(0).getDescription());
        assertEquals(secondComment.getId(), commentList.get(0).getId());

        commentRepository.deleteAllByNewsId(2L);
        List<Comment> deletedList = commentRepository.findAllByNewsId(2L, pageRequest);
        assertNotNull(deletedList);
        assertEquals(0, deletedList.size());
    }
}
