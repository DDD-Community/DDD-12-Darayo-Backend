package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import org.springframework.data.jpa.repository.JpaRepository;

interface PerformanceHallRepository extends JpaRepository<PerformanceHall, Long> {
}
