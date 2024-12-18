package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Prejob;
import myaong.popolog.blogservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrejobRepository extends JpaRepository<Prejob, Long> {

	@Modifying
	@Query("DELETE FROM Prejob p WHERE p.profile=:profile")
	void deleteByProfile(@Param("profile") Profile profile);
}
