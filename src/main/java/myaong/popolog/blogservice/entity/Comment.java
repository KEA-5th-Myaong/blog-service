package myaong.popolog.blogservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`comment`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	// 본 댓글이 달린 포스트
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false, updatable = false)
	private Post post;

	// 댓글 작성자. 작성자 탈퇴 시 null
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", updatable = false)
	private MemberProfile memberProfile;

	// 답글인 경우, 본 답글이 달린 댓글. 댓글인 경우 null
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id", updatable = false)
	private Comment parentComment;

	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_blinded", nullable = false)
	private Boolean isBlinded;

	@Builder
	public Comment(Post post, MemberProfile memberProfile, Comment parentComment, String content, Boolean isBlinded) {
		this.post = post;
		this.memberProfile = memberProfile;
		this.parentComment = parentComment;
		this.content = content;
		this.isBlinded = isBlinded;

		post.getComments().add(this);
	}
}
