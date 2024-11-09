package myaong.popolog.blogservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`member_prejob`",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "job_id"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPrejob extends BaseEntity {

	@Id
	@Column(name = "member_prejob_id")
	private Long id;

	// 회원 아이디
	@Column(name = "member_id", nullable = false, updatable = false)
	private Long memberId;

	// 직군 아이디
	@Column(name = "job_id", nullable = false, updatable = false)
	private Long jobId;

	@Column(name = "job_name", nullable = false)
	private String jobName;

	@Builder
	public MemberPrejob(Long memberId, Long jobId, String jobName) {
		this.memberId = memberId;
		this.jobId = jobId;
		this.jobName = jobName;
	}
}
