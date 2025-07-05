package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.UserAlarmToken;
import ddd.darayo.festival.domain.repository.projection.AlarmTokenProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAlarmTokenRepository extends JpaRepository<UserAlarmToken, Long> {
    @Query("""
    SELECT DISTINCT uat.id as tokenId, uat.alarmToken as alarmToken, upa.targetId as targetId
        FROM UserAlarmToken uat
        JOIN User u ON u.id = uat.userId
        JOIN UserPerformanceAlarm  upa ON u.id = upa.userId
    WHERE u.isAlarmAllowed = true AND upa.targetId in :festivalIds AND uat.isValid = true
""")
    List<AlarmTokenProjection> findAlarmTokens(List<Long> festivalIds);
}
