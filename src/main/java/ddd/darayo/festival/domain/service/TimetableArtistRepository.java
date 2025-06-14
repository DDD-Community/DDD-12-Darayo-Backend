package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.TimetableArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface TimetableArtistRepository extends JpaRepository<TimetableArtist, Long> {
    @Query("SELECT ta FROM TimetableArtist ta WHERE ta.artist.id = :artistId and ta.timetable.id = :timetableId")
    Optional<TimetableArtist> findParticipatingArtist(
            @Param("timetableId") Long timetableId,
            @Param("artistId") Long artistId
    );
}
