package ddd.darayo.festival.presentation.place.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.PerformanceHallContentDTO;
import ddd.darayo.festival.domain.dto.PerformancePlaceContentDTO;
import java.util.List;

public record AddPlaceReq(
        @JsonUnwrapped
        PerformancePlaceContentDTO content,
        List<PerformanceHallContentDTO> placeHalls
) { }
