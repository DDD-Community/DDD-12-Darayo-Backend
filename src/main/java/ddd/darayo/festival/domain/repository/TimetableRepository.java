package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    @Query("""
    SELECT tb
    FROM Timetable tb
    JOIN FETCH tb.artists ta
    JOIN FETCH ta.artist a
    JOIN FETCH tb.hall th
    WHERE tb.performance.id = :festivalId
    """)
    List<Timetable> findByFestivalId(
            @Param("festivalId") Long festivalId
    );
}
