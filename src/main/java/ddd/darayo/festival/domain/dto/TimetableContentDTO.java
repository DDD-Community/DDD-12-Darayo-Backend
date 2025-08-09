package ddd.darayo.festival.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimetableContentDTO(
        LocalDate performanceDate,
        LocalTime startTime,
        LocalTime endTime,
        Long hallId
) { }

