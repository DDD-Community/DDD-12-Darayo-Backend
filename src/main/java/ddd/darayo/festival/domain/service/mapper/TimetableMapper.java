package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.entity.TimetableArtist;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TimetableMapper.TimetableDetailMapper.class})
public interface TimetableMapper {
    @Mapping(target = "performanceHall", source = "timetable.hall.name")
    PerformanceDetailRes.TimeTableDetailRes toTimetableDetail(Timetable timetable);

    @Mapper(componentModel = "spring")
    interface TimetableDetailMapper {
        @Mapping(target = "timetableArtistId", source = "id")
        @Mapping(target = "type", source = "participationType")
        @Mapping(target = "artistId", source = "artist.id") // PerformanceArtist → Artist
        @Mapping(target = "artistName", source = "artist.displayName")
        PerformanceDetailRes.ArtistParticipateDetailRes toArtistParticipateDetail(TimetableArtist timetableArtist);
    }
}
