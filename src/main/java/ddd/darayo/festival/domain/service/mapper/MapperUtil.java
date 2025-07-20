package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.*;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import org.mapstruct.Named;

import java.util.*;

public class MapperUtil {
    @Named("toArtistDetailRes")
    public static List<PerformanceDetailRes.ArtistDetailRes> toArtistDetailRes(Set<Timetable> timetable) {
        Map<Long, PerformanceDetailRes.ArtistDetailRes> artistDetailResMap = new HashMap<>();
        for (Timetable timetableItem : timetable) {
            Set<TimetableArtist> artists = timetableItem.getArtists();
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

    public static List<PerformanceHall> toPlaceHallEntity(List<String> hallNames) {
        return hallNames.stream().map(PerformanceHall::new).toList();
    }

    @Named("fromIdToPerformanceHall")
    public static PerformanceHall fromIdToPerformanceHall(Long hallId) {
        return new PerformanceHall(hallId);
    }

    @Named("fromIdToArtistEntity")
    public static Artist fromIdToArtistEntity(Long artistId) {
        return new Artist(artistId);
    }

    @Named("fromIdToPerformancePlace")
    public static PerformancePlace fromIdToPerformancePlace(Long placeId) {
        return new PerformancePlace(placeId);
    }
}
