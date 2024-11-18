package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Prejob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrejobRepository extends JpaRepository<Prejob, Long> {
}
