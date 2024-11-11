package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.MemberPrejob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPrejobRepository extends JpaRepository<MemberPrejob, Long> {
}
