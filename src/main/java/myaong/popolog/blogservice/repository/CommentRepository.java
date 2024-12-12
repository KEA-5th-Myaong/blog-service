package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Comment;
import myaong.popolog.blogservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPost(Post post);

    // 댓글 ID 목록으로 댓글 조회
    List<Comment> findByIdIn(List<Long> ids);

    @Modifying
    @Query("UPDATE Comment c SET c.isBlinded = :isBlinded WHERE c.id = :commentId")
    void updateBlindedStatus(@Param("commentId") Long commentId, @Param("isBlinded") Boolean isBlinded);

    // 블라인드된 댓글 조회
    List<Comment> findByIsBlindedTrue();
}
