package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.constant.AlarmTargetType;
import ddd.darayo.festival.domain.entity.UserPerformanceAlarm;
import ddd.darayo.festival.domain.exception.constant.AlarmError;
import ddd.darayo.festival.domain.repository.UserPerformanceAlarmRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmSettingManagement {
    private final UserPerformanceAlarmRepository userPerformanceAlarmRepository;

    public void enrollPerformanceAlarm(Long userId, Long performanceId) {
        Optional<UserPerformanceAlarm> upa = userPerformanceAlarmRepository.findByUserIdAndPerformanceId(userId, performanceId);
        if (upa.isPresent()) {
            throw AlarmError.ALARM_ALREADY_FESTIVAL.toException();
        }
        UserPerformanceAlarm userPerformanceAlarm = new UserPerformanceAlarm(null, performanceId, AlarmTargetType.FESTIVAL ,userId);
        userPerformanceAlarmRepository.save(userPerformanceAlarm);
    }

    public void unenrollPerformanceAlarm(Long userId, Long performanceId) {
        UserPerformanceAlarm userPerformanceAlarm = userPerformanceAlarmRepository.findByUserIdAndPerformanceId(userId, performanceId)
                .orElseThrow(AlarmError.ALARM_NOT_EXIST_FESTIVAL::toException);
        userPerformanceAlarmRepository.delete(userPerformanceAlarm);
    }
}
