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
                ArtistMapper.class,
                MapperUtil.class
        }
)
public interface PerformanceMapper {
    @Mapping(target = "placeName", source = "place", qualifiedByName = "toPlaceName")
    @Mapping(target = "placeAddress", source = "place", qualifiedByName = "toPlaceAddress")
    PerformanceDetailRes.PerformanceDetail toPerformanceDetail(Performance performance);

    @Mapping(target = "performance", source = ".")
    @Mapping(target = "timeTables", source = "timetables")
    @Mapping(target = "reservationInfos", source = "reservationInfos")
    @Mapping(target = "artists", source = "timetables", qualifiedByName = "toArtistDetailRes")
    PerformanceDetailRes toPerformanceDetailRes(Performance performance);

    @Mapping(target = "place", source = "placeId", qualifiedByName = "fromIdToPerformancePlace")
    Performance toPerformanceEntity(SavePerformanceReq.PerformanceDTO performanceDTO);

}
