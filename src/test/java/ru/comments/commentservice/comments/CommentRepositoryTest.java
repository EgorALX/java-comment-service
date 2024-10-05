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

        Comment firstComment = new Comment(1, 1, 1, "first description");
        Comment secondComment = new Comment(2, 2, 2, "second description");
        Comment thirdComment = new Comment(3, 3, 3, "third description");

        commentRepository.saveAll(List.of(firstComment, secondComment, thirdComment));

        List<Comment> commentList = commentRepository.findAllByNewsId(2, pageRequest);

        assertNotNull(commentList);
        assertEquals(1, commentList.size());
        assertEquals(secondComment.getUserId(), commentList.get(0).getUserId());
        assertEquals(secondComment.getNewsId(), commentList.get(0).getNewsId());
        assertEquals(secondComment.getDescription(), commentList.get(0).getDescription());
        assertEquals(secondComment.getId(), commentList.get(0).getId());

        commentRepository.deleteAllByNewsId(2);
        List<Comment> deletedList = commentRepository.findAllByNewsId(2, pageRequest);
        assertNotNull(deletedList);
        System.out.println(deletedList);
        assertEquals(0, deletedList.size());
    }
}
