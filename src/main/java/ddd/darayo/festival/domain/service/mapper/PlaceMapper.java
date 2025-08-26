package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.dto.PerformancePlaceContentDTO;
import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.entity.PerformancePlace;
import ddd.darayo.festival.presentation.place.exchanges.GetAllPlaceRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlaceMapper.HallMapper.class})
public interface PlaceMapper {
    @Mapping(target = "name", source = "content.name")
    @Mapping(target = "address", source = "address")
    PerformancePlace toPlaceEntity(PerformancePlaceContentDTO content);

    @Mapping(target = "placeName", source = "name")
    GetAllPlaceRes toGetAllPlaces(PerformancePlace place);

    @Mapper(componentModel = "spring")
    interface HallMapper {
        GetAllPlaceRes.HallInfo toHallInfo(PerformanceHall hall);
    }
}
