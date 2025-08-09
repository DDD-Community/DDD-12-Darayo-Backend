package ddd.darayo.festival.presentation.performance.exchanges;

import com.fasterxml.jackson.annotation.JsonInclude;
import ddd.darayo.festival.domain.constant.ParticipationType;
import ddd.darayo.festival.domain.dto.PerformanceURLContentDTO;
import ddd.darayo.festival.domain.dto.ReservationInfoContentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class SavePerformanceReq {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceDTO {
        private String name;
        private Long placeId;
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
        private Long hallId;
        private List<ArtistParticipateDTO> artists;
    }

    private String password;
    private PerformanceDTO performance;
    private List<TimeTableDTO> timeTables;
    private List<ReservationInfoContentDTO> reservationInfos;
    private List<PerformanceURLContentDTO> urlInfos;
}
