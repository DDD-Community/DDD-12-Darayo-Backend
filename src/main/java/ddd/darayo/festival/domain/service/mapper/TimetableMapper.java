package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.dto.TimetableContentDTO;
import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.entity.TimetableArtist;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetTimetableRes;
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

    @Mapping(target = "performanceHall", source = "hall.name")
    @Mapping(target = "artists", source = "artists")
    @Mapping(target = "timetableId", source = "id")
    UserGetTimetableRes toUserGetTimetable(Timetable timetable);

    @Mapping(target = "hall", source = "hallId", qualifiedByName = "fromIdToPerformanceHall")
    Timetable toTimetableEntity(TimetableContentDTO timetableDTO);


    @Mapper(componentModel = "spring", uses = {MapperUtil.class})
    interface TimetableDetailMapper {
        @Mapping(target = "timetableArtistId", source = "id")
        @Mapping(target = "type", source = "participationType")
        @Mapping(target = "artistId", source = "artist.id")
        @Mapping(target = "artistName", source = "artist.displayName")
        PerformanceDetailRes.ArtistParticipateDetailRes toArtistParticipateDetail(TimetableArtist timetableArtist);

        @Mapping(target = "artistId", source = "artist.id")
        @Mapping(target = "artistDisplayName", source = "artist.displayName")
        @Mapping(target = "artistImageUrl", source = "artist.imageUrl")
        @Mapping(target = "performanceDate", source = "timetable.performanceDate")
        UserGetPerformanceInfo.ArtistDetailRes toArtistDetail(TimetableArtist timetableArtist);

        @Mapping(target = "participationType", source = "type")
        @Mapping(target = "artist", source = "artistId", qualifiedByName = "fromIdToArtistEntity")
        TimetableArtist toTimetableArtist(SavePerformanceReq.ArtistParticipateDTO artistParticipateDTO);

        @Mapping(target = "artistDisplayName", source = "artist.displayName")
        @Mapping(target = "artistId", source = "artist.id")
        @Mapping(target = "type", source = "participationType")
        UserGetTimetableRes.UserTimetableArtistRes toUserTimetableArtist(TimetableArtist timetableArtist);
    }
}
