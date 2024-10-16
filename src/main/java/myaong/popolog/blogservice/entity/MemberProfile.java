package myaong.popolog.blogservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`member_profile`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	// 프로필 사진 주소
	@Column(name = "profile_pic_url")
	private String profilePicUrl;

	@Builder
	public MemberProfile(String nickname, String profilePicUrl) {
		this.nickname = nickname;
		this.profilePicUrl = profilePicUrl;
	}
}
