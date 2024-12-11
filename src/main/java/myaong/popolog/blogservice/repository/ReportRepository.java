package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Report;
import myaong.popolog.blogservice.enums.ContentsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByProfileIdAndContentsIdAndContentsType(Long memberId, Long contentsId, ContentsType contentsType);
}
