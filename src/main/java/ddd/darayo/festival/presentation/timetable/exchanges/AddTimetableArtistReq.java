package ddd.darayo.festival.presentation.timetable.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.TimetableArtistContentDTO;

public record AddTimetableArtistReq(
        Long artistId,
        @JsonUnwrapped
        TimetableArtistContentDTO content
) { }
