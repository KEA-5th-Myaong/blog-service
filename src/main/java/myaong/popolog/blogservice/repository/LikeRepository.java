package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
