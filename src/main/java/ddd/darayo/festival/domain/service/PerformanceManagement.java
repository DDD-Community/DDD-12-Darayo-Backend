package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.dto.EditPerformanceDTO;
import ddd.darayo.festival.domain.dto.EditReservationInfoCommand;
import ddd.darayo.festival.domain.dto.PerformanceURLContentDTO;
import ddd.darayo.festival.domain.dto.ReservationInfoContentDTO;
import ddd.darayo.festival.domain.entity.*;
import ddd.darayo.festival.domain.exception.constant.PerformanceError;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.PerformanceURLRepository;
import ddd.darayo.festival.domain.repository.ReservationInfoRepository;
import ddd.darayo.festival.domain.service.mapper.PerformanceMapper;
import ddd.darayo.festival.domain.service.mapper.ReservationInfoMapper;
import ddd.darayo.festival.domain.service.mapper.TimetableMapper;
import ddd.darayo.festival.domain.service.mapper.URLMapper;
import ddd.darayo.festival.infra.aop.TouchPerformanceUpdatedAt;
import ddd.darayo.festival.presentation.performance.exchanges.EditReservationInfoReq;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class  PerformanceManagement {
    private final PerformanceRepository performanceRepository;
    private final PerformanceURLRepository performanceURLRepository;
    private final ReservationInfoRepository reservationInfoRepository;

    private final PerformanceMapper performanceMapper;
    private final URLMapper urlMapper;

    private final TimetableMapper timetableMapper;
    private final ReservationInfoMapper reservationInfoMapper;
    private final TimetableMapper.TimetableDetailMapper timetableDetailMapper;

    public Performance save(SavePerformanceReq dto, LocalDateTime now) {
        Performance performance = this.performanceMapper.toPerformanceEntity(dto.getPerformance().getContent());

        // 타임테이블 추가
        dto.getTimeTables().forEach(timeTableDTO -> {
            Timetable timetable = timetableMapper.toTimetableEntity(timeTableDTO.getContent());
            timeTableDTO.getArtists().forEach(artist -> {
                TimetableArtist timetableArtist = timetableDetailMapper.toTimetableArtist(artist);
                timetable.addArtist(timetableArtist);
            });
            performance.addTimetable(timetable);
        });

        // 예약 정보 추가
        dto.getReservationInfos().forEach(reservationInfoDTO -> {
            ReservationInfo reservationInfo = reservationInfoMapper.toReservationInfo(reservationInfoDTO, now);
            performance.addReservationInfo(reservationInfo);
        });

        dto.getUrlInfos().forEach(urlDTO -> {
            PerformanceURL url = urlMapper.toPerformanceURL(urlDTO);
            performance.addUrl(url);
        });
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
            Set<UserGetPerformanceInfo.ArtistDetailRes> artistSet = new HashSet<>();
            Set<Timetable> timetables = performance.getTimetables();

            for (Timetable timetable : timetables) {
                for (TimetableArtist timetableArtist : timetable.getArtists()) {
                    UserGetPerformanceInfo.ArtistDetailRes artist = timetableDetailMapper.toArtistDetail(timetableArtist);
                    artistSet.add(artist);
                }
            }

            dto.artists().addAll(artistSet);
            userGetPerformanceInfos.add(dto);
        }
        return userGetPerformanceInfos;
    }

    public void delete(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);

        performanceRepository.delete(performance);
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PERFORMANCE_ID, key = "#performanceId")
    public void addReservationInfo(Long performanceId, ReservationInfoContentDTO dto) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);

        ReservationInfo reservationInfo = reservationInfoMapper.toReservationInfo(dto, LocalDateTime.now());
        performance.addReservationInfo(reservationInfo);
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PERFORMANCE_ID, key = "#performanceId")
    public void addPerformanceURL(Long performanceId, PerformanceURLContentDTO dto) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);
        PerformanceURL performanceURL = urlMapper.toPerformanceURL(dto);
        performance.addUrl(performanceURL);
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PERFORMANCE_URL_ID, key = "#performanceURLId")
    public void updatePerformanceURL(Long performanceURLId, PerformanceURLContentDTO dto) {
        PerformanceURL performanceURL = performanceURLRepository.findById(performanceURLId)
                .orElseThrow(PerformanceError.PERFORMANCE_URL_NOT_EXIST::toException);
        performanceURL.update(dto.url(), dto.type());
    } 

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PERFORMANCE_ID, key = "#performanceId")
    public void updateReservationInfo(Long performanceId, List<EditReservationInfoReq> reqList, LocalDateTime now) {
        val performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);

        Map<Long, ReservationInfo> existingMap = performance.getReservationInfos().stream()
                .filter(info -> info.getId() != null)
                .collect(Collectors.toMap(ReservationInfo::getId, Function.identity()));

        Set<ReservationInfo> newReservationInfos = new HashSet<>();
        for (val req : reqList) {
            if (req.id() != null && existingMap.containsKey(req.id())) {
                ReservationInfo existing = existingMap.get(req.id());
                existing.updateWith(req.command(), now); // 아래에 정의
                newReservationInfos.add(existing);
            } else {
                ReservationInfo newInfo = new ReservationInfo(
                        req.command().openDateTime(),
                        req.command().closeDateTime(),
                        req.command().ticketURL(),
                        req.command().type(),
                        req.command().remark(),
                        now
                );
                newInfo.setPerformance(performance);
                newReservationInfos.add(newInfo);
            }
        }

        performance.getReservationInfos().clear();
        performance.getReservationInfos().addAll(newReservationInfos);
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PERFORMANCE_ID, key = "#performanceId")
    public void updatePerformance(Long performanceId, EditPerformanceDTO req) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(PerformanceError.PERFORMANCE_NOT_EXIST::toException);

        performance.update(req);
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.RESERVATION_INFO_ID, key = "#reservationInfoId")
    public void updateReservationInfo(Long reservationInfoId, EditReservationInfoCommand command, LocalDateTime now) {
        ReservationInfo reservationInfo = reservationInfoRepository.findById(reservationInfoId)
                .orElseThrow(PerformanceError.PERFORMANCE_RESERVATION_INFO_NOT_EXIST::toException);

        reservationInfo.updateWith(command, now);
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.RESERVATION_INFO_ID, key = "#reservationId")
    public void deleteReservationInfo(Long reservationId) {
        ReservationInfo reservationInfo = reservationInfoRepository.findById(reservationId)
                .orElseThrow(PerformanceError.PERFORMANCE_RESERVATION_INFO_NOT_EXIST::toException);
        reservationInfoRepository.delete(reservationInfo);
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PERFORMANCE_URL_ID, key = "#performanceURLId")
    public void deletePerformanceURL(Long performanceURLId) {
        performanceURLRepository.findById(performanceURLId)
                .orElseThrow(PerformanceError.PERFORMANCE_URL_NOT_EXIST::toException);

        PerformanceURL url = performanceURLRepository.getReferenceById(performanceURLId);
        performanceURLRepository.delete(url);
    }
}
