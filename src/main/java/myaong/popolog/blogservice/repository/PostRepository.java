package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findTop10ByOrderByIdDesc();
}
