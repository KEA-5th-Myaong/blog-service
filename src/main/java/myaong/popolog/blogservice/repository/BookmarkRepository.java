package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Bookmark;
import myaong.popolog.blogservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Boolean existsByIdAndProfile(Long id, Profile profile);
}
