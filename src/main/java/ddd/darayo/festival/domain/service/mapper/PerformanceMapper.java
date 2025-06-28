package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.entity.PerformanceURL;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                TimetableMapper.class,
                ReservationInfoMapper.class,
                URLMapper.class,
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
    @Mapping(target = "urlInfos", source = "urls")
    @Mapping(target = "reservationInfos", source = "reservationInfos")
    @Mapping(target = "artists", source = "timetables", qualifiedByName = "toArtistDetailRes")
    PerformanceDetailRes toPerformanceDetailRes(Performance performance);

    @Mapping(target = "place", source = "placeId", qualifiedByName = "fromIdToPerformancePlace")
    Performance toPerformanceEntity(SavePerformanceReq.PerformanceDTO performanceDTO);

    @Mapping(target = "festivalId", source = "id")
    @Mapping(target = "urlInfos", source = "urls")
    @Mapping(target = "placeName", source = "place.name")
    @Mapping(target = "placeAddress", source = "place.address")
    @Mapping(target = "artists", expression = "java(new ArrayList<>())") // artist 정보는 후처리를 통해 채워넣습니다.
    UserGetPerformanceInfo toUserGetPerformanceInfo(Performance performance);
}
