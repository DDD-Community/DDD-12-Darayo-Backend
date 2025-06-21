package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceHallRepository extends JpaRepository<PerformanceHall, Long> {
}