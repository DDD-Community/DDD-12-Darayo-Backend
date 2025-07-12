package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.UserPerformanceAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPerformanceAlarmRepository extends JpaRepository<UserPerformanceAlarm, Long> {
    @Query("""
    SELECT upa
    FROM UserPerformanceAlarm upa
    WHERE upa.userId = :userId AND upa.targetId = :performanceId
""")
    Optional<UserPerformanceAlarm> findByUserIdAndPerformanceId(
            @Param("userId") Long userId,
            @Param("performanceId") Long performanceId
    );
}
