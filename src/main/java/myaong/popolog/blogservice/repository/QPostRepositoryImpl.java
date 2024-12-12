package myaong.popolog.blogservice.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.dto.SortedEntity;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.entity.QPrejob;
import myaong.popolog.blogservice.service.ProfileQueryService;
import org.springframework.stereotype.Repository;

import java.util.List;

import static myaong.popolog.blogservice.entity.QBookmark.bookmark;
import static myaong.popolog.blogservice.entity.QPrejob.prejob;
import static myaong.popolog.blogservice.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final ProfileQueryService profileQueryService;

	@Override
	public List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs) {

		QPrejob reqPrejob = new QPrejob("reqPrejob");

		return jpaQueryFactory
				.selectFrom(post)
				.where(JPAExpressions.selectOne()
						.from(prejob)
						.join(reqPrejob).on(reqPrejob.jobId.eq(prejob.jobId))
						.where(prejob.profile.eq(member),
								reqPrejob.profile.eq(post.profile),
								post.profile.ne(member))
						.exists())
				.orderBy(post.id.desc())
				.limit(10)
				.fetch();
	}

	@Override
	public List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs, Long lastId) {

		QPrejob reqPrejob = new QPrejob("reqPrejob");

		return jpaQueryFactory
				.selectFrom(post)
				.where(JPAExpressions.selectOne()
						.from(prejob)
						.join(reqPrejob)
						.on(reqPrejob.jobId.eq(prejob.jobId))
						.where(prejob.profile.eq(member),
								reqPrejob.profile.eq(post.profile),
								post.profile.ne(member),
								post.id.lt(lastId))
						.exists())
				.orderBy(post.id.desc())
				.limit(10)
				.fetch();
	}

	@Override
	public List<Post> findByFollowing(Profile member) {

		return jpaQueryFactory
				.selectFrom(post)
				.where(post.profile.in(profileQueryService.findFollowingOf(member)))
				.orderBy(post.id.desc())
				.limit(10)
				.fetch();
	}

	@Override
	public List<Post> findByFollowing(Profile member, Long lastId) {

		return jpaQueryFactory
				.selectFrom(post)
				.where(post.profile.in(profileQueryService.findFollowingOf(member)),
						post.id.lt(lastId))
				.orderBy(post.id.desc())
				.limit(10)
				.fetch();
	}

	@Override
	public List<SortedEntity<Post>> findByProfile_Bookmark(Profile member) {

		return jpaQueryFactory
				.select(post, bookmark.id)
				.from(post)
				.join(bookmark)
				.on(post.id.eq(bookmark.post.id))
				.where(bookmark.profile.eq(member))
				.orderBy(bookmark.id.desc())
				.limit(10)
				.fetch()
				.stream()
				.map(tuple -> new SortedEntity<>(tuple.get(post), tuple.get(bookmark.id)))
				.toList();
	}

	@Override
	public List<SortedEntity<Post>> findByProfile_Bookmark(Profile member, Long lastId) {

		return jpaQueryFactory
				.select(post, bookmark.id)
				.from(post)
				.join(bookmark)
				.on(post.id.eq(bookmark.post.id))
				.where(bookmark.profile.eq(member),
						bookmark.id.lt(lastId))
				.orderBy(bookmark.id.desc())
				.limit(10)
				.fetch()
				.stream()
				.map(tuple -> new SortedEntity<>(tuple.get(post), tuple.get(bookmark.id)))
				.toList();
	}
}
