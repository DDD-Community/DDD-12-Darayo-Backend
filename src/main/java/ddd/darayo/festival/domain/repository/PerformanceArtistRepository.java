package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.PerformanceArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerformanceArtistRepository extends JpaRepository<PerformanceArtist, Long> {
    @Query("SELECT pa FROM PerformanceArtist pa WHERE pa.performance.id = :performanceId and pa.artist.id = :artistId")
    Optional<PerformanceArtist> findParticipatingArtist(
            @Param("performanceId") Long performanceId,
            @Param("artistId") Long artistId
    );
}
