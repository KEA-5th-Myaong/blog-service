package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Bookmark;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	Boolean existsByPostAndProfile(Post post, Profile profile);
	@Query("SELECT COUNT(b) > 0 FROM Bookmark b WHERE b.post = :post AND b.profile.id = :memberId")
	Boolean existsByPostAndMemberId(@Param("post") Post post, @Param("memberId") Long memberId);
	Optional<Bookmark> findByPostAndProfile(Post post, Profile profile);
}
