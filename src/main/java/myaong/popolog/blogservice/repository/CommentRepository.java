package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
