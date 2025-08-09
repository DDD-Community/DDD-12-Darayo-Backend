package ddd.darayo.festival.presentation.performance.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.constant.ParticipationType;
import ddd.darayo.festival.domain.dto.PerformanceURLContentDTO;
import ddd.darayo.festival.domain.dto.ReservationInfoContentDTO;
import ddd.darayo.festival.domain.dto.TimetableContentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
        @JsonUnwrapped
        private TimetableContentDTO content;
        private List<ArtistParticipateDTO> artists;
    }

    private String password;
    private PerformanceDTO performance;
    private List<TimeTableDTO> timeTables;
    private List<ReservationInfoContentDTO> reservationInfos;
    private List<PerformanceURLContentDTO> urlInfos;
}
