package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.PerformancePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformancePlaceRepository extends JpaRepository<PerformancePlace, Long> {
}
