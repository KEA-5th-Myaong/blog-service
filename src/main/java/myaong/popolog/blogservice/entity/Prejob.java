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
public class Prejob extends BaseEntity {

	@Id
	@Column(name = "member_prejob_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, updatable = false)
	private Profile profile;

	// 직군 아이디
	@Column(name = "job_id", nullable = false, updatable = false)
	private Long jobId;

	@Column(name = "job_name", nullable = false)
	private String jobName;

	@Builder
	public Prejob(Long id, Profile profile, Long jobId, String jobName) {
		this.id = id;
		this.profile = profile;
		this.jobId = jobId;
		this.jobName = jobName;
	}
}
