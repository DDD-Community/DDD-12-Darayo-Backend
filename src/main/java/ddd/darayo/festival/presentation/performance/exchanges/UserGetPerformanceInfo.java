package ddd.darayo.festival.presentation.performance.exchanges;


import ddd.darayo.festival.domain.constant.ReservationType;
import ddd.darayo.festival.domain.constant.URLType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserGetPerformanceInfo(
        Long festivalId,
        String name,
        String placeName,
        String placeAddress,
        LocalDate startDate,
        LocalDate endDate,
        String posterUrl,
        String banGoods,
        String transportationInfo,
        String remark,
        LocalDateTime updatedAt,
        List<ReservationInfoDetailRes> reservationInfos,
        List<ArtistDetailRes> artists,
        List<PerformanceURLRes> urlInfos
) {
    public record ReservationInfoDetailRes(
            Long reservationInfoId,
            LocalDateTime openDateTime,
            LocalDateTime closeDateTime,
            String ticketURL,
            ReservationType type,
            String remark
    ) { }

    public record ArtistDetailRes(
            Long artistId,
            String artistDisplayName,
            String artistImageUrl,
            LocalDate performanceDate
    ) { }

    public record PerformanceURLRes(
            String url,
            URLType type
    ){}
}
