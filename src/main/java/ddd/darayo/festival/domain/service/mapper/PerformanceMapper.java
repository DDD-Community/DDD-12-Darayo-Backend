package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                TimetableMapper.class,
                ReservationInfoMapper.class,
                ArtistMapper.class
        }
)
public interface PerformanceMapper {

    PerformanceDetailRes.PerformanceDetail toPerformanceDetail(Performance performance);

    @Mapping(target = "performance", source = ".")
    @Mapping(target = "timeTables", source = "timetables")
    @Mapping(target = "reservationInfos", source = "reservationInfos")
    @Mapping(target = "artists", source = "artists")
    PerformanceDetailRes toPerformanceDetailRes(Performance performance);
}
