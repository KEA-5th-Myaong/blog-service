package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
