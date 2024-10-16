package myaong.popolog.blogservice.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`bookmark`",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "post_id"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookmark_id")
	private Long id;

	// 북마크한 회원 => 회원 정보 자체를 쓸 일은 없음(중복만 확인)
	@Column(name = "member_id", updatable = false)
	private Long memberId;

	// 대상 포스트
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", updatable = false)
	private Post post;

	@Builder
	public Bookmark(Long memberId, Post post) {
		this.memberId = memberId;
		this.post = post;
	}
}
