package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findTop10ByOrderByIdDesc();
	List<Post> findTop10ByIdLessThanOrderByIdDesc(Long lastId);
}
