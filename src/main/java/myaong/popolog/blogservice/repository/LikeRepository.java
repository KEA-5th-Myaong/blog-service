package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Like;
import myaong.popolog.blogservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndMemberId(Long postId, Long memberId);
    boolean existsByPostAndMemberId(Post post, Long memberId);
    // 게시물의 좋아요 수 조회
    long countByPost(Post post);
}
