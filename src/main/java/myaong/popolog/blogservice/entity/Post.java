package myaong.popolog.blogservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "`post`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	// 작성자 정보
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberProfile memberProfile;

	@Column(name = "title", nullable = false)
	private String title;

	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	// 섬네일 주소
	@Column(name = "thumbnail_url")
	private String thumbnailUrl;

	@Column(name = "is_blinded", nullable = false)
	private Boolean isBlinded;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	//***** cascade 설정 *****//
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Like> likes;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bookmark> bookmarks;
	//***** cascade 설정 끝 *****//

	@Builder
	public Post(MemberProfile memberProfile, String title, String content, String thumbnailUrl, Boolean isBlinded) {
		this.memberProfile = memberProfile;
		this.title = title;
		this.content = content;
		this.thumbnailUrl = thumbnailUrl;
		this.isBlinded = isBlinded;
	}
}
