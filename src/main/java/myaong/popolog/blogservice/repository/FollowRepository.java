package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

	boolean existsByFollowingAndFollowed(Profile following, Profile followed);
	void deleteByFollowingAndFollowed(Profile following, Profile followed);
}
