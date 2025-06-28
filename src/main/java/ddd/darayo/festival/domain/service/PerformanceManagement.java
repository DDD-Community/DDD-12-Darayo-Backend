package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.*;
import ddd.darayo.festival.domain.exception.constant.PerformanceError;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.service.mapper.PerformanceMapper;
import ddd.darayo.festival.domain.service.mapper.ReservationInfoMapper;
import ddd.darayo.festival.domain.service.mapper.TimetableMapper;
import ddd.darayo.festival.domain.service.mapper.URLMapper;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class PerformanceManagement {
    private final PerformanceRepository performanceRepository;
    private final PerformanceMapper performanceMapper;
    private final URLMapper urlMapper;

    private final TimetableMapper timetableMapper;
    private final ReservationInfoMapper reservationInfoMapper;
    private final TimetableMapper.TimetableDetailMapper timetableDetailMapper;

    public Performance save(SavePerformanceReq dto) {
        Performance performance = this.performanceMapper.toPerformanceEntity(dto.getPerformance());

        // 타임테이블 추가
        dto.getTimeTables().forEach(timeTableDTO -> {
            Timetable timetable = timetableMapper.toTimetableEntity(timeTableDTO);
            timeTableDTO.getArtists().forEach(artist -> {
                TimetableArtist timetableArtist = timetableDetailMapper.toTimetableArtist(artist);
                timetable.addArtist(timetableArtist);
            });
            performance.addTimetable(timetable);
        });

        // 예약 정보 추가
        dto.getReservationInfos().forEach(reservationInfoDTO -> {
            ReservationInfo reservationInfo = reservationInfoMapper.toReservationEntity(reservationInfoDTO);
            performance.addReservationInfo(reservationInfo);
        });

//        dto.getUrlInfos().forEach(urlDTO -> {
//            PerformanceURL url = urlMapper.toPerformanceURL(urlDTO);
//            performance.addUrl(url);
//        });

        return performanceRepository.save(performance);
    }


    public List<PerformanceDetailRes> findAllDetail() {
        List<Performance> performances = performanceRepository.findAllDetail();
        return performances
                .stream()
                .map(performanceMapper::toPerformanceDetailRes)
                .toList();
    }

    public List<UserGetPerformanceInfo> findUserPerformance() {
        List<Performance> performances = performanceRepository.findAllDetail();
        List<UserGetPerformanceInfo> userGetPerformanceInfos = new ArrayList<>();
        for (Performance performance : performances) {
            UserGetPerformanceInfo dto = performanceMapper.toUserGetPerformanceInfo(performance);
            Set<Timetable> timetables = performance.getTimetables();
            for (Timetable timetable : timetables) {
                for (TimetableArtist timetableArtist : timetable.getArtists()) {
                    UserGetPerformanceInfo.ArtistDetailRes artist = timetableDetailMapper.toArtistDetail(timetableArtist);
                    dto.artists().add(artist);
                }
            }
            userGetPerformanceInfos.add(dto);
        }
        return userGetPerformanceInfos;
    }

    public void delete(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);

        performanceRepository.delete(performance);
    }
}
