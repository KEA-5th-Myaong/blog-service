package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

	List<MemberProfile> findByIdIn(List<Long> ids);
}
