package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.PerformanceURL;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface URLMapper {
    PerformanceURL toPerformanceURL(SavePerformanceReq.PerformanceURLDTO urlInfo);
}