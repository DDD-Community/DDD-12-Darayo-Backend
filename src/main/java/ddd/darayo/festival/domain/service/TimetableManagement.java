package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.constant.ParticipationType;
import ddd.darayo.festival.domain.entity.Artist;
import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.entity.TimetableArtist;
import ddd.darayo.festival.domain.exception.constant.PerformanceError;
import ddd.darayo.festival.domain.exception.constant.PlaceError;
import ddd.darayo.festival.domain.exception.constant.TimetableError;
import ddd.darayo.festival.domain.repository.*;
import ddd.darayo.festival.presentation.performance.exchanges.AddTimetableReq;
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
    private final TimetableRepository timetableRepository;
    private final TimetableArtistRepository timetableArtistRepository;
    private final PerformanceHallRepository performanceHallRepository;

    public Timetable addTimetable(Long performanceId, AddTimetableReq req) {
        if (!performanceRepository.existsById(performanceId)) {
            throw PerformanceError.PERFORMANCE_NOT_EXIST.toException();
        }
        // TODO: 같은 홀, 같은 시간 겹치는 경우
        Timetable timetable = new Timetable(req.performanceDate(), req.startTime(), req.endTime(), req.hallId());
        return timetableRepository.save(timetable);
    }


    public void putTimetableArtist(Long timetableId, Long artistId, ParticipationType type) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(TimetableError.TIMETABLE_NOT_EXISTS::toException);
        Optional<TimetableArtist> ota = timetableArtistRepository.findParticipatingArtist(timetableId, artistId);
        if (ota.isPresent()) {
            TimetableArtist artist = ota.get();
            artist.setParticipationType(type);
        } else {
            TimetableArtist ta = new TimetableArtist(null, type, new Artist(artistId), timetable);
            timetableArtistRepository.save(ta);
        }
    }

    public void editTimetable(Long timetableId, EditTimetableReq req) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(TimetableError.TIMETABLE_NOT_EXISTS::toException);
        if (performanceHallRepository.existsById(req.hallId())) {
            throw PlaceError.PLACE_HALL_NOT_EXIST.toException();
        }
        timetable.update(req.performanceDate(), req.startTime(), req.endTime(), new PerformanceHall(req.hallId()));
    }

    public void deleteTimetableArtist(Long timetableId, Long artistId) {
        int result = timetableArtistRepository.deleteTimetableArtist(timetableId, artistId);
        if (result < 1) {
            throw TimetableError.TIMETABLE_ARTIST_NOT_EXISTS.toException();
        }
    }

}
