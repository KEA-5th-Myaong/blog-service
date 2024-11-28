package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, QProfileRepository {

	Optional<Profile> findByUsername(String username);

	List<Profile> findByIdIn(List<Long> ids);
}
