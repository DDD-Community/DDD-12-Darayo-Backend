package ddd.darayo.festival.presentation.performance.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.TimetableContentDTO;

public record AddTimetableReq(
        @JsonUnwrapped
        TimetableContentDTO content
) { }
