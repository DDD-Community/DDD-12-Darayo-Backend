package ddd.darayo.festival.application.usecase.performance;

import ddd.darayo.festival.application.usecase.common.Params;
import ddd.darayo.festival.application.usecase.common.UseCase;
import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.entity.TimetableArtist;
import ddd.darayo.festival.domain.entity.UserPerformanceAlarm;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.TimetableRepository;
import ddd.darayo.festival.domain.repository.UserPerformanceAlarmRepository;
import ddd.darayo.festival.domain.service.mapper.PerformanceMapper;
import ddd.darayo.festival.domain.service.mapper.TimetableMapper;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GetAlarmedFestivalUseCase implements UseCase<GetAlarmedFestivalUseCase.Param, List<UserGetPerformanceInfo>> {
    private final UserPerformanceAlarmRepository userPerformanceAlarmRepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceMapper performanceMapper;
    private final TimetableMapper.TimetableDetailMapper timetableDetailMapper;

    public record Param(
            Long userId
    ) implements Params {}

    @Override
    @Transactional
    public List<UserGetPerformanceInfo> execute(Param params) {
        List<Long> performanceIds = userPerformanceAlarmRepository.findTargetIdsByUserId(params.userId);
        List<Performance> performances = performanceRepository.findAllDetail(performanceIds);

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
}
