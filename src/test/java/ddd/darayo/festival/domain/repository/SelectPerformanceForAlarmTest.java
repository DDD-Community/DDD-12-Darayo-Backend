package ddd.darayo.festival.domain.repository;


import ddd.darayo.festival.common.ReadonlyContainerBaseTest;
import ddd.darayo.festival.common.ServiceTest;
import ddd.darayo.festival.domain.entity.Performance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
public class SelectPerformanceForAlarmTest extends ReadonlyContainerBaseTest {
    @Autowired
    private PerformanceRepository performanceRepository;

    @Test
    void 예매_1일후_공연_정보를_조회해야_합니다_다중_조회_성공() {
        LocalDateTime now = LocalDateTime.of(2024, 6, 14,0, 0, 0);
        LocalDateTime tomorrow = now.plusDays(1);
        LocalDateTime tomorrowEnd = tomorrow.plusDays(1).minusSeconds(1);

        List<Performance> performances = performanceRepository.findByReservationOpenDateBetween(tomorrow, tomorrowEnd);

        Assertions.assertEquals(3, performances.size());
        Assertions.assertEquals(4, performances.get(0).getId());
        Assertions.assertEquals(5, performances.get(1).getId());
        Assertions.assertEquals(6, performances.get(2).getId());
    }

    @Test
    void 예매_1일후_공연_정보를_조회해야_합니다_0건_조회_성공() {
        LocalDateTime now = LocalDateTime.of(2024, 6, 13,0, 0, 0);
        LocalDateTime tomorrow = now.plusDays(1);
        LocalDateTime tomorrowEnd = tomorrow.plusDays(1).minusSeconds(1);

        List<Performance> performances = performanceRepository.findByReservationOpenDateBetween(tomorrow, tomorrowEnd);

        Assertions.assertEquals(0, performances.size());
    }

    @Test
    void 예매_1일후_공연_정보를_조회해야_합니다_1건_조회_성공() {
        LocalDateTime now = LocalDateTime.of(2024, 7, 31,0, 0, 0);
        LocalDateTime tomorrow = now.plusDays(1);
        LocalDateTime tomorrowEnd = tomorrow.plusDays(1).minusSeconds(1);

        List<Performance> performances = performanceRepository.findByReservationOpenDateBetween(tomorrow, tomorrowEnd);

        Assertions.assertEquals(1, performances.size());
        Assertions.assertEquals(3, performances.get(0).getId());
    }
}
