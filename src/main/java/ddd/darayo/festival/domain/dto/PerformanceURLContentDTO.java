package ddd.darayo.festival.domain.dto;

import ddd.darayo.festival.domain.constant.URLType;

public record PerformanceURLContentDTO(
    String url,
    URLType type
) {}
