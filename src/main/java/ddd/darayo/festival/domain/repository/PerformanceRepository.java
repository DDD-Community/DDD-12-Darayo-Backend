package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    @Query("""
        SELECT DISTINCT p FROM Performance p
            LEFT JOIN FETCH p.timetables t
            LEFT JOIN FETCH t.artists ta
            LEFT JOIN FETCH ta.artist a
    """)
    List<Performance> findAllDetail();

}
