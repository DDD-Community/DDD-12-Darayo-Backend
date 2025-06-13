package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.exception.constant.PerformanceError;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.TimetableRepository;
import ddd.darayo.festival.presentation.performance.exchanges.AddTimetableReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class TimetableManagement {
    private final PerformanceRepository performanceRepository;
    private final TimetableRepository timetableRepository;

    public Timetable addTimetable(Long performanceId, AddTimetableReq req) {
        if (!performanceRepository.existsById(performanceId)) {
            throw PerformanceError.PERFORMANCE_NOT_EXIST.toException();
        }
        // TODO: 같은 홀, 같은 시간 겹치는 경우
        Timetable timetable = new Timetable(req.performanceDate(), req.startTime(), req.endTime(), req.performanceHall());
        return timetableRepository.save(timetable);
    }

}
