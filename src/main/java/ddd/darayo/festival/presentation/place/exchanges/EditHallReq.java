package ddd.darayo.festival.presentation.place.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.PerformanceHallContentDTO;

public record EditHallReq(
    @JsonUnwrapped
    PerformanceHallContentDTO content
) { }
