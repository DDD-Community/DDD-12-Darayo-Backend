package ddd.darayo.festival.presentation.place.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.PerformancePlaceContentDTO;

public record EditPlaceReq(
        @JsonUnwrapped
        PerformancePlaceContentDTO content
) { }
