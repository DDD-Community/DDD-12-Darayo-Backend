package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.PerformanceArtist;
import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.exception.constant.PerformanceError;
import ddd.darayo.festival.domain.exception.constant.TimetableError;
import ddd.darayo.festival.domain.repository.PerformanceArtistRepository;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.TimetableRepository;
import ddd.darayo.festival.presentation.performance.exchanges.AddTimetableReq;
import ddd.darayo.festival.presentation.performance.exchanges.AddPerformanceArtistReq;
import ddd.darayo.festival.presentation.timetable.exchanges.EditTimetableReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class TimetableManagement {
    private final PerformanceRepository performanceRepository;
    private final PerformanceArtistRepository performanceArtistRepository;
    private final TimetableRepository timetableRepository;

    public Timetable addTimetable(Long performanceId, AddTimetableReq req) {
        if (!performanceRepository.existsById(performanceId)) {
            throw PerformanceError.PERFORMANCE_NOT_EXIST.toException();
        }
        // TODO: 같은 홀, 같은 시간 겹치는 경우
        Timetable timetable = new Timetable(req.performanceDate(), req.startTime(), req.endTime(), req.performanceHall());
        return timetableRepository.save(timetable);
    }

    public void addArtist(Long performanceId, AddPerformanceArtistReq req) {
        if (!performanceRepository.existsById(performanceId)) {
            throw PerformanceError.PERFORMANCE_NOT_EXIST.toException();
        }
        Optional<PerformanceArtist> opa = performanceArtistRepository.findParticipatingArtist(performanceId, req.artistId());
        if (opa.isPresent()) {
            throw PerformanceError.PERFORMANCE_ARTIST_ALREADY_EXISTS.toException();
        }
        PerformanceArtist artist = new PerformanceArtist(performanceId, req.artistId());
        performanceArtistRepository.save(artist);
    }

    public void editTimetable(Long timetableId, EditTimetableReq req) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(TimetableError.TIMETABLE_NOT_EXISTS::toException);
        timetable.update(req.performanceDate(), req.startTime(), req.endTime(), req.performanceHall());
    }

}
