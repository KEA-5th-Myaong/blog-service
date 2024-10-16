package myaong.popolog.blogservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`like`",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "post_id"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_id")
	private Long id;

	// 좋아요를 남긴 회원
	@Column(name = "member_id", nullable = false, updatable = false)
	private Long memberId;

	// 대상 포스트
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false, updatable = false)
	private Post post;

	@Builder
	public Like(Long memberId, Post post) {
		this.memberId = memberId;
		this.post = post;
	}
}
