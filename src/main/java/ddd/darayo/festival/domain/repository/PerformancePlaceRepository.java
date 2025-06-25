package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.PerformancePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformancePlaceRepository extends JpaRepository<PerformancePlace, Long> {
    @Query("SELECT DISTINCT pp " +
            "FROM PerformancePlace pp " +
            "JOIN FETCH pp.halls")
    List<PerformancePlace> findAllPlacesFetched();
}
