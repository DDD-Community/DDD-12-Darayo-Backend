package ddd.darayo.festival.presentation.performance.exchanges;

import ddd.darayo.festival.domain.constant.ParticipationType;
import ddd.darayo.festival.domain.constant.ReservationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record PerformanceDetailRes(
    PerformanceDetail performance,
    List<TimeTableDetailRes> timeTables,
    List<ReservationInfoDetailRes> reservationInfos,
    List<ArtistDetailRes> artists
) {

    public record PerformanceDetail(
        Long id,
        String name,
        String placeName,
        String placeAddress,
        LocalDate startDate,
        LocalDate endDate,
        String posterUrl,
        String banGoods,
        String transportationInfo,
        String remark
    ) {}

    public record TimeTableDetailRes(
        Long id,
        LocalDate performanceDate,
        LocalTime startTime,
        LocalTime endTime,
        String performanceHall,
        List<ArtistParticipateDetailRes> artists
    ) { }

    public record ReservationInfoDetailRes(
        Long id,
        LocalDateTime openDateTime,
        LocalDateTime closeDateTime,
        String ticketURL,
        ReservationType type,
        String remark
    ) { }

    public record ArtistDetailRes(
        Long id,
        String displayName
    ) { }

    public record ArtistParticipateDetailRes(
            Long timetableArtistId,
            Long artistId,
            String artistName,
            ParticipationType type
    ) { }
}