package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

	boolean existsByFollowingAndFollowed(MemberProfile following, MemberProfile followed);
	void deleteByFollowingAndFollowed(MemberProfile following, MemberProfile followed);
}
