package ddd.darayo.festival.domain.dto;

import java.time.LocalDate;

public record PerformanceContentDTO(
    String name,
    Long placeId,
    LocalDate startDate,
    LocalDate endDate,
    String posterUrl,
    String banGoods,
    String transportationInfo,
    String remark
) { }


