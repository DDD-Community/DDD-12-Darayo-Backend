package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.*;
import ddd.darayo.festival.domain.exception.constant.PerformanceError;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.projection.PerformanceDetailProjection;
import ddd.darayo.festival.domain.service.mapper.PerformanceMapper;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
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

    private Performance from(SavePerformanceReq.PerformanceDTO dto) {
        return Performance.builder()
                .name(dto.getName())
                .placeName(dto.getPlaceName())
                .placeAddress(dto.getPlaceAddress())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .posterUrl(dto.getPosterUrl())
                .remark(dto.getRemark())
                .transportationInfo(dto.getTransportationInfo())
                .banGoods(dto.getBanGoods())
                .build();
    }

    private Timetable from(
            SavePerformanceReq.TimeTableDTO dto,
            Performance performance
    ) {
        Map<Long, PerformanceArtist> artistMap = new HashMap<>();

        Timetable timetable = new Timetable(
            dto.getPerformanceDate(),
            dto.getStartTime(),
            dto.getEndTime(),
            dto.getPerformanceHall()
        );
        dto.getArtists().forEach(artist -> {
            PerformanceArtist performanceArtist = artistMap.computeIfAbsent(artist.getArtistId(), id -> {
                PerformanceArtist pa = new PerformanceArtist(id);
                performance.addArtist(pa);
                return pa;
            });
            TimetableArtist timetableArtist = new TimetableArtist(null, artist.getType(), performanceArtist, null);
            timetable.addArtist(timetableArtist);
        });
        return timetable;
    }


    private ReservationInfo from(SavePerformanceReq.ReservationInfoDTO dto) {
        return new ReservationInfo(
                dto.getOpenDateTime(),
                dto.getCloseDateTime(),
                dto.getTicketURL(),
                dto.getType(),
                dto.getRemark()
        );
    }

    public Performance save(SavePerformanceReq dto) {
        Performance performance = from(dto.getPerformance());

        // 타임테이블 추가
        if (dto.getTimeTables() != null) {
            dto.getTimeTables().forEach(timeTableDTO -> {
                Timetable timetable = from(timeTableDTO, performance);
                performance.addTimetable(timetable);
            });
        }

        // 예약 정보 추가
        if (dto.getReservationInfos() != null) {
            dto.getReservationInfos().forEach(reservationInfoDTO -> {
                ReservationInfo reservationInfo = from(reservationInfoDTO);
                performance.addReservationInfo(reservationInfo);
            });
        }

        return performanceRepository.save(performance);
    }


    public List<PerformanceDetailRes> findAllDetail() {
        List<Performance> performances = performanceRepository.findAllDetail();
        return performances
                .stream()
                .map(performanceMapper::toPerformanceDetailRes)
                .toList();
    }

    public void delete(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);

        performanceRepository.delete(performance);
    }
}
