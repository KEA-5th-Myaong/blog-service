package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.dto.SortedEntity;
import myaong.popolog.blogservice.entity.Profile;

import java.util.List;

public interface QProfileRepository {

	/**
	 * @param member Follow.following
	 * @param lastId nullable. 마지막으로 조회된 Follow id
	 * @return {@code member}가 팔로우하고 있는 프로필 10개. Follow.followed
	 */
	List<SortedEntity<Profile>> findProfilesFollowedBy(Profile member, Long lastId);

	/**
	 * @param member Follow.followed
	 * @param lastId nullable. 마지막으로 조회된 Follow id
	 * @return {@code member}를 팔로우하는 프로필 10개. Follow.following
	 */
	List<SortedEntity<Profile>> findProfilesFollowing(Profile member, Long lastId);
}
