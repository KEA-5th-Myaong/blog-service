package myaong.popolog.blogservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`member_profile`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {

	@Id
	@Column(name = "member_id")
	private Long id;

	// 로그인 아이디
	@Column(name = "username", nullable = false)
	private String username;

	// 본명
	@Column(name = "name", nullable = false)
	private String name;

	// 닉네임
	@Column(name = "nickname", nullable = false)
	private String nickname;

	// 프로필 사진 주소
	@Column(name = "profile_pic_url")
	private String profilePicUrl;

	@Column(name = "blog_intro", nullable = false)
	private String blogIntro;

	// 내가 팔로우하는 사람 목록
	@OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follow> followings = new ArrayList<>();

	// 나를 팔로우하는 사람 목록
	@OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follow> followers = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bookmark> bookmarks = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Report> reports = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Prejob> prejobs = new ArrayList<>();

	@Builder
	public Profile(Long id, String username, String name, String nickname, String profilePicUrl, String blogIntro) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.nickname = nickname;
		this.profilePicUrl = profilePicUrl;
		this.blogIntro = blogIntro;
	}

	public void updateProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}
}
