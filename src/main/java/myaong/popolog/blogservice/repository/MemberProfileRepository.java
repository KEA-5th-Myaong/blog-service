package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
}
