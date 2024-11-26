package myaong.popolog.blogservice.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.entity.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

import static myaong.popolog.blogservice.entity.QFollow.follow;
import static myaong.popolog.blogservice.entity.QProfile.profile;

@Repository
@RequiredArgsConstructor
public class QProfileRepositoryImpl implements QProfileRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Tuple> findProfilesFollowedBy(Profile member, Long lastId) {

		// member = following, result = followed
		BooleanBuilder condition = new BooleanBuilder().and(follow.following.eq(member));
		if (lastId != null) {
			condition.and(follow.id.lt(lastId));
		}

		return jpaQueryFactory
				.select(profile, follow.id)
				.from(profile)
				.join(follow)
				.on(follow.followed.eq(profile))
				.where(condition)
				.orderBy(follow.id.desc())
				.limit(10)
				.fetch();
	}

	@Override
	public List<Tuple> findProfilesFollowing(Profile member, Long lastId) {

		// member = followed, result = following
		BooleanBuilder condition = new BooleanBuilder().and(follow.followed.eq(member));
		if (lastId != null) {
			condition.and(follow.id.lt(lastId));
		}

		return jpaQueryFactory
				.select(profile, follow.id)
				.from(profile)
				.join(follow)
				.on(follow.following.eq(profile))
				.where(condition)
				.orderBy(follow.id.desc())
				.limit(10)
				.fetch();
	}
}
