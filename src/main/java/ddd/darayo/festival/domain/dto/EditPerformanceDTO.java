package ddd.darayo.festival.domain.dto;

import java.time.LocalDate;

public record EditPerformanceDTO(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String posterUrl,
        String banGoods,
        String transportationInfo,
        String remark
) { }
