package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
