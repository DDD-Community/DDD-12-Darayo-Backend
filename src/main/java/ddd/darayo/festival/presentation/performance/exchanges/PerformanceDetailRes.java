package ddd.darayo.festival.presentation.performance.exchanges;

import ddd.darayo.festival.domain.constant.ParticipationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDetailRes {
    private Long id;
    private String name;
    private String placeName;
    private String placeAddress;
    private LocalDate startDate;
    private LocalDate endDate;
    private String posterUrl;
    private String banGoods;
    private String transportationInfo;
    private String remark;
    private List<TimeTableDetailRes> timeTables;
    private List<ReservationInfoDetailRes> reservationInfos;
    private List<ArtistDetailRes> artists;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeTableDetailRes {
        private Long id;
        private LocalDate performanceDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String performanceHall;
        private List<ArtistParticipateDetailRes> artists;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationInfoDetailRes {
        private Long id;
        private LocalDateTime openDateTime;
        private LocalDateTime closeDateTime;
        private String ticketURL;
        private String type;
        private String remark;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistDetailRes {
        private Long id;
        private String displayName;
        private LocalDate date;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistParticipateDetailRes {
        private Long timetableArtistId;
        private Long artistId;
        private String artistName;
        private ParticipationType type;
    }
} 