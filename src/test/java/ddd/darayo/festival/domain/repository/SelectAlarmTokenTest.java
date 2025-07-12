package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.common.ReadonlyContainerBaseTest;
import ddd.darayo.festival.common.ServiceTest;
import ddd.darayo.festival.domain.repository.projection.AlarmTokenProjection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ServiceTest
@DisplayName("공연 아이디 기반 알림 토큰 조회 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class SelectAlarmTokenTest extends ReadonlyContainerBaseTest {
    @Autowired private UserAlarmTokenRepository userAlarmTokenRepository;

    @Test
    void 알림을_설정한_공연에_대해서만_알림_토큰을_조회한다() {
        List<AlarmTokenProjection> alarmTokens = userAlarmTokenRepository.findAlarmTokens(List.of(1L, 2L));

        Assertions.assertEquals(4, alarmTokens.size());
        Assertions.assertEquals(2, alarmTokens.stream().filter(token -> token.getTokenId() == 1L).count());
        Assertions.assertEquals(2, alarmTokens.stream().filter(token -> token.getTokenId() == 2L).count());
    }

    @Test
    void 유효하지_않은_토큰은_조회하지_않는다() {
        List<AlarmTokenProjection> alarmTokens = userAlarmTokenRepository.findAlarmTokens(List.of(4L));

        Assertions.assertEquals(0, alarmTokens.size());
    }
}
