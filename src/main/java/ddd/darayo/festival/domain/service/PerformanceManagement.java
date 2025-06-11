package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.*;
import ddd.darayo.festival.domain.exception.constant.PerformanceError;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.projection.PerformanceDetailProjection;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
                dto.getDate() == null ? LocalDate.of(0, 1, 1)  : dto.getDate(),
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


    public List<PerformanceDetailRes> findAllDetail() {
        List<PerformanceDetailProjection> flatResults = performanceRepository.findAllDetail();

        Map<Long, PerformanceDetailRes> performanceMap = new LinkedHashMap<>();

        for (PerformanceDetailProjection row : flatResults) {
            PerformanceDetailRes pRes = performanceMap.computeIfAbsent(row.getPerformanceId(), performanceId -> new PerformanceDetailRes(
                    performanceId,
                    row.getPerformanceName(),
                    row.getPlaceName(),
                    row.getPlaceAddress(),
                    row.getStartDate(),
                    row.getEndDate(),
                    row.getPosterUrl(),
                    row.getBanGoods(),
                    row.getTransportationInfo(),
                    row.getPerformanceRemark(),
                    new ArrayList<>(), // timeTables
                    new ArrayList<>(), // reservationInfos
                    new ArrayList<>()  // artists
            ));

            // Timetable 정보 조립
            if (row.getTimetableId() != null) {
                PerformanceDetailRes.TimeTableDetailRes ttRes = pRes.getTimeTables().stream()
                        .filter(tt -> tt.getId().equals(row.getTimetableId()))
                        .findFirst()
                        .orElseGet(() -> {
                            PerformanceDetailRes.TimeTableDetailRes newTt = new PerformanceDetailRes.TimeTableDetailRes(
                                    row.getTimetableId(),
                                    row.getTimetablePerformanceDate(),
                                    row.getTimetableStartTime(),
                                    row.getTimetableEndTime(),
                                    row.getTimetablePerformanceHall(),
                                    new ArrayList<>()
                            );
                            pRes.getTimeTables().add(newTt);
                            return newTt;
                        });

                // Timetable에 참여하는 Artist 정보 조립
                if (row.getTimetableArtistId() != null) {
                    PerformanceDetailRes.ArtistParticipateDetailRes ttArtist = new PerformanceDetailRes.ArtistParticipateDetailRes(
                            row.getTimetableArtistId(),
                            row.getTimetableArtistArtistId(),
                            row.getTimetableArtistName(),
                            row.getTimetableArtistType()
                    );
                    if (ttRes.getArtists().stream().noneMatch(a -> a.getTimetableArtistId().equals(ttArtist.getTimetableArtistId()) && a.getArtistId().equals(ttArtist.getArtistId()) && a.getType() == ttArtist.getType())) {
                        ttRes.getArtists().add(ttArtist);
                    }
                }
            }

            // ReservationInfo 정보 조립
            if (row.getReservationInfoId() != null) {
                PerformanceDetailRes.ReservationInfoDetailRes rInfoRes = new PerformanceDetailRes.ReservationInfoDetailRes(
                        row.getReservationInfoId(),
                        row.getReservationInfoOpenDateTime(),
                        row.getReservationInfoCloseDateTime(),
                        row.getReservationInfoTicketURL(),
                        row.getReservationInfoType() != null ? row.getReservationInfoType().name() : null,
                        row.getReservationInfoRemark()
                );
                if (pRes.getReservationInfos().stream().noneMatch(r -> r.getId().equals(rInfoRes.getId()))) {
                    pRes.getReservationInfos().add(rInfoRes);
                }
            }

            // Performance-level Artist 정보 조립
            if (row.getPerformanceArtistId() != null) {
                PerformanceDetailRes.ArtistDetailRes pArtistRes = new PerformanceDetailRes.ArtistDetailRes(
                        row.getPerformanceArtistId(),
                        row.getPerformanceArtistDisplayName(),
                        row.getPerformanceArtistDate()
                );
                if (pRes.getArtists().stream().noneMatch(a -> a.getId().equals(pArtistRes.getId()))) {
                    pRes.getArtists().add(pArtistRes);
                }
            }
        }
        return new ArrayList<>(performanceMap.values());
    }

    public void delete(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);

        performanceRepository.delete(performance);
    }
}
