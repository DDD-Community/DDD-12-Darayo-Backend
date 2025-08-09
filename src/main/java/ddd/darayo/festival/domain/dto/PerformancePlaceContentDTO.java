package ddd.darayo.festival.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PerformancePlaceContentDTO(
    @JsonProperty("placeName") String name,
    String address
) { }


