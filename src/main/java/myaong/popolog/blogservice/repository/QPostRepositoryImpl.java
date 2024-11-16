package myaong.popolog.blogservice.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.entity.QPrejob;
import org.springframework.stereotype.Repository;

import java.util.List;

import static myaong.popolog.blogservice.entity.QPrejob.prejob;
import static myaong.popolog.blogservice.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Post> findByPrejobExcludingProfile(Profile member, List<Long> preJobs) {

		QPrejob reqPrejob = new QPrejob("reqPrejob");

		return jpaQueryFactory
				.selectFrom(post)
				.where(JPAExpressions.selectOne()
						.from(prejob)
						.join(reqPrejob).on(reqPrejob.jobId.eq(prejob.jobId))
						.where(prejob.profile.eq(member).and(reqPrejob.profile.eq(post.profile)).and(post.profile.ne(member)))
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
						.where(prejob.profile.eq(member).and(reqPrejob.profile.eq(post.profile)).and(post.profile.ne(member)).and(post.id.lt(lastId)))
						.exists())
				.orderBy(post.id.desc())
				.limit(10)
				.fetch();
	}
}
