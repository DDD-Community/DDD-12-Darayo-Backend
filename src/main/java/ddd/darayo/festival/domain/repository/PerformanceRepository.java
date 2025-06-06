package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.repository.projection.PerformanceDetailProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    @Query("SELECT DISTINCT " +
            "p.id as performanceId, p.name as performanceName, p.placeName as placeName, p.placeAddress as placeAddress, " +
            "p.startDate as startDate, p.endDate as endDate, p.posterUrl as posterUrl, p.banGoods as banGoods, " +
            "p.transportationInfo as transportationInfo, p.remark as performanceRemark, " +
            "t.id as timetableId, t.performanceDate as timetablePerformanceDate, t.startTime as timetableStartTime, t.endTime as timetableEndTime, t.performanceHall as timetablePerformanceHall, " +
            "ta.id as timetableArtistId, ta.artist.id as timetableArtistArtistId, ta.artist.displayName as timetableArtistName, ta.participationType as timetableArtistType, " +
            "r.id as reservationInfoId, r.openDateTime as reservationInfoOpenDateTime, r.closeDateTime as reservationInfoCloseDateTime, r.ticketURL as reservationInfoTicketURL, r.type as reservationInfoType, r.remark as reservationInfoRemark, " +
            "pa.id as performanceArtistId, pa.artist.displayName as performanceArtistDisplayName, pa.performanceDate as performanceArtistDate " +
            "FROM Performance p " +
            "LEFT JOIN p.timetables t " +
            "LEFT JOIN t.artists ta " +
            "LEFT JOIN p.reservationInfos r " +
            "LEFT JOIN p.artists pa ")
    List<PerformanceDetailProjection> findAllDetail();

}
