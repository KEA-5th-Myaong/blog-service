package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.dto.SortedEntity;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;

import java.util.List;

public interface QPostRepository {

	List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs);

	List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs, Long lastId);

	List<Post> findByFollowing(Profile member);

	List<Post> findByFollowing(Profile member, Long lastId);

	List<SortedEntity<Post>> findByProfile_Bookmark(Profile member);

	List<SortedEntity<Post>> findByProfile_Bookmark(Profile member, Long lastId);

	List<Post> searchByTitleAndContent(String query);

	List<Post> searchByTitleAndContent(String query, Long lastId);
}
