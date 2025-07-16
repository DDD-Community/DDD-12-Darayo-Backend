package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.ReservationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationInfoRepository extends JpaRepository<ReservationInfo, Long> {
    @Query("""
    SELECT DISTINCT ri
    FROM ReservationInfo ri
    JOIN FETCH ri.performance
    WHERE :fromDate < ri.openTimeModifiedAt AND ri.openTimeModifiedAt <= :toDate
""")
    List<ReservationInfo> findUpdatedReservationInfos(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

}
