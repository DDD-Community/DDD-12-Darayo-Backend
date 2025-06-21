package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.TimetableArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TimetableArtistRepository extends JpaRepository<TimetableArtist, Long> {
    @Query("SELECT ta FROM TimetableArtist ta WHERE ta.artist.id = :artistId and ta.timetable.id = :timetableId")
    Optional<TimetableArtist> findParticipatingArtist(
            @Param("timetableId") Long timetableId,
            @Param("artistId") Long artistId
    );

    @Modifying
    @Query("DELETE FROM TimetableArtist ta WHERE ta.timetable.id = :timetableId and ta.artist.id = :artistId")
    int deleteTimetableArtist(
            @Param("timetableId") Long timetableId,
            @Param("artistId") Long artistId
    );
}
