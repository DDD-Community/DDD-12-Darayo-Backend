package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.constant.ReservationType;
import ddd.darayo.festival.domain.entity.*;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class PerformanceManagement {
    private final PerformanceRepository performanceRepository;


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

    private Timetable from(SavePerformanceReq.TimeTableDTO dto) {
        Timetable timetable = new Timetable(
            dto.getPerformanceDate(),
            dto.getStartTime(),
            dto.getEndTime(),
            dto.getPerformanceHall()
        );
        dto.getArtists().forEach(artist -> {
            Artist artistEntity = new Artist(artist.getArtistId());
            TimetableArtist timetableArtist = new TimetableArtist(null, artist.getType(), artistEntity, null);
            timetable.addArtist(timetableArtist);
        });
        return timetable;
    }

    private PerformanceArtist from(SavePerformanceReq.PerformanceArtistDTO dto) {
        Artist artist = new Artist(dto.getId()); // displayName은 null로 설정
        return new PerformanceArtist(
                dto.getDate(),
                artist
        );
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
                Timetable timetable = from(timeTableDTO);
                performance.addTimetable(timetable);
            });
        }

        // 아티스트 정보 추가
        if (dto.getArtists() != null) {
            dto.getArtists().forEach(artistDTO -> {
                PerformanceArtist performanceArtist = from(artistDTO);
                performance.addArtist(performanceArtist);
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
}
