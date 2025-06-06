package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
}
