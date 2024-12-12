package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, QPostRepository {

	// 최신 게시물 10개 조회
	List<Post> findTop10ByOrderByIdDesc();

	// 특정 ID보다 작은 최신 게시물 10개 조회
	List<Post> findTop10ByIdLessThanOrderByIdDesc(Long lastId);

	@Query("SELECT p FROM Post p JOIN p.profile pr WHERE pr.username = :username AND p.title = :title")
	Optional<Post> findByUsernameAndTitle(@Param("username") String username, @Param("title") String title);

	List<Post> findTop10ByProfileAndIdLessThanOrderByIdDesc(Profile profile, Long lastId);

	List<Post> findTop10ByProfileOrderByIdDesc(Profile profile);

	// 블라인드된 게시물 조회
	List<Post> findByIsBlindedTrueOrderByIdDesc();

	// 특정 ID보다 작은 블라인드된 게시물 조회
	List<Post> findByIsBlindedTrueAndIdLessThanOrderByIdDesc(Long lastId);

	// 특정 ID 목록에 해당하는 게시물 조회 (for ReportedContents)
	@Query("SELECT p FROM Post p WHERE p.id IN :postIds")
	List<Post> findPostsByIds(@Param("postIds") List<Long> postIds);
}
