package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, QPostRepository {

	List<Post> findTop10ByOrderByIdDesc();
	List<Post> findTop10ByIdLessThanOrderByIdDesc(Long lastId);
	@Query("SELECT p FROM Post p JOIN p.profile pr WHERE pr.username = :username AND p.title = :title")
	Optional<Post> findByUsernameAndTitle(@Param("username") String username, @Param("title") String title);
	List<Post> findTop10ByProfile_IdAndIdLessThanOrderByIdDesc(Long profileId, Long lastId);
}
