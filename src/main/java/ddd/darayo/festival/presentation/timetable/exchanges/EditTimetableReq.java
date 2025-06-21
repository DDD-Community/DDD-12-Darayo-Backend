package ddd.darayo.festival.presentation.timetable.exchanges;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalTime;

public record EditTimetableReq(
        LocalDate performanceDate,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalTime startTime,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalTime endTime,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long hallId
) { }
