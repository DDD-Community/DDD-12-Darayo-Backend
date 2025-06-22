package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.entity.TimetableArtist;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        TimetableMapper.TimetableDetailMapper.class,
        MapperUtil.class
})
public interface TimetableMapper {
    @Mapping(target = "performanceHall", source = "timetable.hall.name")
    PerformanceDetailRes.TimeTableDetailRes toTimetableDetail(Timetable timetable);

    @Mapping(target = "hall", source = "hallId", qualifiedByName = "fromIdToPerformanceHall")
    Timetable toTimetableEntity(SavePerformanceReq.TimeTableDTO timetableDTO);

    @Mapper(componentModel = "spring", uses = {MapperUtil.class})
    interface TimetableDetailMapper {
        @Mapping(target = "timetableArtistId", source = "id")
        @Mapping(target = "type", source = "participationType")
        @Mapping(target = "artistId", source = "artist.id")
        @Mapping(target = "artistName", source = "artist.displayName")
        PerformanceDetailRes.ArtistParticipateDetailRes toArtistParticipateDetail(TimetableArtist timetableArtist);

        @Mapping(target = "participationType", source = "type")
        @Mapping(target = "artist", source = "artistId", qualifiedByName = "fromIdToArtistEntity")
        TimetableArtist toTimetableArtist(SavePerformanceReq.ArtistParticipateDTO artistParticipateDTO);
    }
}
