package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.dto.PerformanceURLContentDTO;
import ddd.darayo.festival.domain.entity.PerformanceURL;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface URLMapper {
    PerformanceURL toPerformanceURL(PerformanceURLContentDTO dto);

    UserGetPerformanceInfo.PerformanceURLRes toUserGetPerformanceInfo(PerformanceURL url);

    PerformanceDetailRes.UrlDetailRes toUrlDetailRes(PerformanceURL url);
}
