package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.constant.ParticipationType;
import ddd.darayo.festival.domain.entity.*;
import ddd.darayo.festival.domain.exception.constant.PerformanceError;
import ddd.darayo.festival.domain.exception.constant.PlaceError;
import ddd.darayo.festival.domain.exception.constant.TimetableError;
import ddd.darayo.festival.domain.repository.*;
import ddd.darayo.festival.domain.service.mapper.TimetableMapper;
import ddd.darayo.festival.infra.aop.TouchPerformanceUpdatedAt;
import ddd.darayo.festival.presentation.performance.exchanges.AddTimetableReq;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetTimetableRes;
import ddd.darayo.festival.presentation.timetable.exchanges.EditTimetableReq;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class TimetableManagement {
    private final PerformanceRepository performanceRepository;
    private final TimetableRepository timetableRepository;
    private final TimetableArtistRepository timetableArtistRepository;
    private final PerformanceHallRepository performanceHallRepository;

    private final TimetableMapper timetableMapper;

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PERFORMANCE_ID, key = "#performanceId")
    public Timetable addTimetable(Long performanceId, AddTimetableReq req) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);

        Timetable timetable = timetableMapper.toTimetableEntity(req.content());
        timetable.setPerformance(performance);
        return timetableRepository.save(timetable);
    }


    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.TIMETABLE_ID, key = "#timetableId")
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

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.TIMETABLE_ID, key = "#timetableId")
    public void editTimetable(Long timetableId, EditTimetableReq req) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(TimetableError.TIMETABLE_NOT_EXISTS::toException);
        if (req.content().hallId() != null && !performanceHallRepository.existsById(req.content().hallId())) {
            throw PlaceError.PLACE_HALL_NOT_EXIST.toException();
        }
        timetable.update(
                req.content().performanceDate(),
                req.content().startTime(),
                req.content().endTime(),
                new PerformanceHall(req.content().hallId())
        );
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.TIMETABLE_ID, key = "#timetableId")
    public void deleteTimetableArtist(Long timetableId, Long artistId) {
        int result = timetableArtistRepository.deleteTimetableArtist(timetableId, artistId);
        if (result < 1) {
            throw TimetableError.TIMETABLE_ARTIST_NOT_EXISTS.toException();
        }
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(TimetableError.TIMETABLE_NOT_EXISTS::toException);
    }

    public List<UserGetTimetableRes> getUserGetTimetables(Long festivalId) {
        List<Timetable> timetables = timetableRepository.findByFestivalId(festivalId);
        return timetables.stream().map(timetableMapper::toUserGetTimetable).toList();
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.TIMETABLE_ID, key = "#timetableId")
    public void deleteTimetable(Long timetableId) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(TimetableError.TIMETABLE_NOT_EXISTS::toException);
        Performance performance = timetable.getPerformance();
        timetableRepository.delete(timetable);
    }
}
