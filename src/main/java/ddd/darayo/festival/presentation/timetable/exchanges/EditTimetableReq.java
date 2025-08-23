package ddd.darayo.festival.presentation.timetable.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.TimetableContentDTO;

public record EditTimetableReq(
        @JsonUnwrapped
        TimetableContentDTO content
) { }
