package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    @Query("""
        SELECT DISTINCT p FROM Performance p
            LEFT JOIN FETCH p.timetables t
            LEFT JOIN FETCH t.artists ta
            LEFT JOIN FETCH ta.artist a
            LEFT JOIN FETCH p.place pp
            LEFT JOIN FETCH p.urls pu
    """)
    List<Performance> findAllDetail();

    @Query("""
        SELECT p FROM Performance p
            LEFT JOIN FETCH p.timetables t
            LEFT JOIN FETCH t.artists ta
            LEFT JOIN FETCH ta.artist a
            LEFT JOIN FETCH p.place pp
            LEFT JOIN FETCH p.urls pu
         WHERE p.id = :festivalId
    """)
    Optional<Performance> findPerformanceDetailById(
            @Param("festivalId") Long festivalId
    );

    @Query("""
        SELECT DISTINCT p FROM Performance p
            LEFT JOIN FETCH p.timetables t
            LEFT JOIN FETCH t.artists ta
            LEFT JOIN FETCH ta.artist a
            LEFT JOIN FETCH p.place pp
            LEFT JOIN FETCH p.urls pu
            WHERE p.id in :performanceIds
    """)
    List<Performance> findAllDetail(
            @Param("performanceIds") List<Long> performanceIds
    );

    @Query("""
        SELECT DISTINCT p
            FROM Performance p
            JOIN FETCH p.reservationInfos r
        WHERE r.openDateTime BETWEEN :start AND :end
    """)
    List<Performance> findByReservationOpenDateBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT p
            FROM Performance p
        WHERE p.startDate = :date
    """)
    List<Performance> findByDate(@Param("date") LocalDate date);

    // 모든 공연장소(Place)에 연결된 공연 조회
    List<Performance> findByPlace_Id(Long placeId);

    @Query("""
        SELECT DISTINCT p FROM Performance p
            JOIN p.timetables t
            JOIN t.hall h
        WHERE h.id = :hallId
    """)
    List<Performance> findByHallId(@Param("hallId") Long hallId);
}
