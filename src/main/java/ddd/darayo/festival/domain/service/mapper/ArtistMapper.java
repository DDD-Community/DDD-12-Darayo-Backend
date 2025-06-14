package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.PerformanceArtist;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    @Mapping(target = "id", source = "artist.id")
    @Mapping(target = "displayName", source = "artist.displayName")
    PerformanceDetailRes.ArtistDetailRes toPerformanceDetailRes(PerformanceArtist artist);
}
