package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.PerformanceURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceURLRepository extends JpaRepository<PerformanceURL, Long> {
}
