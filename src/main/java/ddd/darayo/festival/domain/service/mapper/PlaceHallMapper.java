package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceHallMapper {
    PerformanceHall toHallEntity(String name);
}
