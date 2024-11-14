package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;

import java.util.List;

public interface QPostRepository {

	List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs);
	List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs, Long lastId);
}
