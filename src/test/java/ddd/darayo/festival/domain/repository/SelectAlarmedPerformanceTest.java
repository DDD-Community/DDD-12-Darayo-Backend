package ddd.darayo.festival.domain.repository;


import ddd.darayo.festival.application.usecase.performance.GetAlarmedFestivalUseCase;
import ddd.darayo.festival.common.ReadonlyContainerBaseTest;
import ddd.darayo.festival.common.ServiceTest;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ServiceTest
@DisplayName("알림 설정한 공연 정보 조회 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class SelectAlarmedPerformanceTest extends ReadonlyContainerBaseTest {
    @Autowired private GetAlarmedFestivalUseCase getAlarmedFestivalUseCase;

    @Test
    public void 알림을_설정한_공연정보만_조회해야_합니다() {
        List<UserGetPerformanceInfo> result = getAlarmedFestivalUseCase.execute(new GetAlarmedFestivalUseCase.Param(1L));

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(item -> item.festivalId().equals(1L)));
        Assertions.assertTrue(result.stream().anyMatch(item -> item.festivalId().equals(2L)));
    }

    @Test
    public void 알림을_설정하지_않으면_NULL이_아닌_빈_배열을_조회합니다() {
        List<UserGetPerformanceInfo> result = getAlarmedFestivalUseCase.execute(new GetAlarmedFestivalUseCase.Param(5L));

        Assertions.assertEquals(0, result.size());
    }
}
