package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, QPostRepository {

	// 최신 게시물 10개 조회
	List<Post> findTop10ByOrderByIdDesc();

	// 특정 ID보다 작은 최신 게시물 10개 조회
	List<Post> findTop10ByIdLessThanOrderByIdDesc(Long lastId);

	// 블라인드된 게시물 조회
	List<Post> findByIsBlindedTrueOrderByIdDesc();

	// 특정 ID보다 작은 블라인드된 게시물 조회
	List<Post> findByIsBlindedTrueAndIdLessThanOrderByIdDesc(Long lastId);

	// 특정 ID 목록에 해당하는 게시물 조회 (for ReportedContents)
	@Query("SELECT p FROM Post p WHERE p.id IN :postIds")
	List<Post> findPostsByIds(@Param("postIds") List<Long> postIds);

	@Modifying
	@Query("UPDATE Post p SET p.isBlinded = :isBlinded WHERE p.id = :postId")
	void updateBlindedStatus(@Param("postId") Long postId, @Param("isBlinded") Boolean isBlinded);
}

