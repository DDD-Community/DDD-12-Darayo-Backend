package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.PerformancePlace;
import ddd.darayo.festival.presentation.place.exchanges.AddPlaceReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    @Mapping(target = "name", source = "placeName")
    PerformancePlace toPlaceEntity(AddPlaceReq req);
}
