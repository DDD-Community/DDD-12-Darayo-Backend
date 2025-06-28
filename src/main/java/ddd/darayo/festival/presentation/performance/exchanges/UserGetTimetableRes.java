package ddd.darayo.festival.presentation.performance.exchanges;

import ddd.darayo.festival.domain.constant.ParticipationType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record UserGetTimetableRes(
    Long timetableId,
    LocalDate performanceDate,
    LocalTime startTime,
    LocalTime endTime,
    String performanceHall,
    List<UserTimetableArtistRes> artists
) {
    public record UserTimetableArtistRes(
            Long artistId,
            String artistDisplayName,
            ParticipationType type
    ) { }
}
