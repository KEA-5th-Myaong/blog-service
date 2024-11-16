package myaong.popolog.blogservice.repository;

import com.querydsl.core.Tuple;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;

import java.util.List;

public interface QPostRepository {

	List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs);

	List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs, Long lastId);

	List<Post> findByFollowing(Profile member);

	List<Post> findByFollowing(Profile member, Long lastId);

	List<Tuple> findByProfile_Bookmark(Profile member);

	List<Tuple> findByProfile_Bookmark(Profile member, Long lastId);
}
