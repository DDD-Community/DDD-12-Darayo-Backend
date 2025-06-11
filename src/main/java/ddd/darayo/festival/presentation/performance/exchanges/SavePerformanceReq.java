package ddd.darayo.festival.presentation.performance.exchanges;

import com.fasterxml.jackson.annotation.JsonInclude;
import ddd.darayo.festival.domain.constant.ParticipationType;
import ddd.darayo.festival.domain.constant.ReservationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class SavePerformanceReq {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceDTO {
        private String name;
        private String placeName;
        private String placeAddress;
        private LocalDate startDate;
        private LocalDate endDate;
        private String posterUrl;

        private String banGoods;
        private String transportationInfo;
        private String remark;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistParticipateDTO {
        private Long artistId;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ParticipationType type;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeTableDTO {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalDate performanceDate;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalTime startTime;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalTime endTime;
        private String performanceHall;
        private List<ArtistParticipateDTO> artists;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationInfoDTO {
        private LocalDateTime openDateTime;
        private LocalDateTime closeDateTime;
        private ReservationType type;
        private String ticketURL;
        private String remark;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceArtistDTO {
        private Long id;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private LocalDate date;
    }

    private String password;
    private PerformanceDTO performance;
    private List<TimeTableDTO> timeTables;
    private List<ReservationInfoDTO> reservationInfos;
}
