package myaong.popolog.blogservice.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`follow`",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"following", "followed"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private Long id;

	// 팔로우하는 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following", nullable = false, updatable = false)
	private MemberProfile following;

	// 팔로우된 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "followed", nullable = false, updatable = false)
	private MemberProfile followed;

	@Builder
	public Follow(MemberProfile following, MemberProfile followed) {
		this.following = following;
		this.followed = followed;

		following.getFollowings().add(this);
		followed.getFollowers().add(this);
	}
}
