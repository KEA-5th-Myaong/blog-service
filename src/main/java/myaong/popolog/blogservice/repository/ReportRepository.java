package myaong.popolog.blogservice.repository;

import myaong.popolog.blogservice.entity.Report;
import myaong.popolog.blogservice.enums.ContentsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE (:lastId = 0 OR r.id < :lastId) ORDER BY r.id DESC")
    List<Report> findByLastId(@Param("lastId") Long lastId);

    @Query("SELECT r.contentsId, COUNT(r) FROM Report r GROUP BY r.contentsId")
    List<Object[]> countReportsByContents();
}

