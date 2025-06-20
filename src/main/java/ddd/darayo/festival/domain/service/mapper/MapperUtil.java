package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.PerformancePlace;
import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.entity.TimetableArtist;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import org.mapstruct.Named;

import java.util.*;

public class MapperUtil {
    @Named("toArtistDetailRes")
    public static List<PerformanceDetailRes.ArtistDetailRes> toArtistDetailRes(Set<Timetable> timetable) {
        Map<Long, PerformanceDetailRes.ArtistDetailRes> artistDetailResMap = new HashMap<>();
        for (Timetable timetableItem : timetable) {
            List<TimetableArtist> artists = timetableItem.getArtists();
            for (TimetableArtist artist: artists) {
                artistDetailResMap.put(
                        artist.getId(),
                        new PerformanceDetailRes.ArtistDetailRes(artist.getId(), artist.getArtist().getDisplayName())
                );
            }
        }
        return new ArrayList<>(artistDetailResMap.values());
    }
    @Named("toPlaceName")
    public static String toPlaceName(PerformancePlace performancePlace) {
        return performancePlace.getName();
    }
    @Named("toPlaceAddress")
    public static String toPlaceAddress(PerformancePlace performancePlace) {
        return performancePlace.getAddress();
    }
}
