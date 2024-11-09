package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	List<Profile> findByIdIn(List<Long> ids);
}
