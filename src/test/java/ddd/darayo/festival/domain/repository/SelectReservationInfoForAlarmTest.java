package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.common.ReadonlyContainerBaseTest;
import ddd.darayo.festival.common.ServiceTest;
import ddd.darayo.festival.domain.entity.ReservationInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;


@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("예약 정보 갱신 알림 대상 공연 정보 테스트")
public class SelectReservationInfoForAlarmTest extends ReadonlyContainerBaseTest {
    @Autowired private ReservationInfoRepository reservationInfoRepository;

    @Test
    public void 발송일_기준_19시_포함_이전_내역만_조회해야_한다() {
        LocalDateTime toDate = LocalDateTime.of(2024, 6, 4, 19, 0, 0);
        LocalDateTime fromDate = toDate.minusDays(1);

        List<ReservationInfo> reservationInfo = reservationInfoRepository.findUpdatedReservationInfos(fromDate, toDate);
        Assertions.assertEquals(2, reservationInfo.size());
        Assertions.assertEquals(7, reservationInfo.get(0).getId());
        Assertions.assertEquals(8, reservationInfo.get(1).getId());
    }

    @Test
    public void 전날_19시_이후에_갱신된_내역을_같이_조회한다() {
        LocalDateTime toDate = LocalDateTime.of(2024, 6, 9, 19, 0, 0);
        LocalDateTime fromDate = toDate.minusDays(1);

        List<ReservationInfo> reservationInfo = reservationInfoRepository.findUpdatedReservationInfos(fromDate, toDate);
        Assertions.assertEquals(3, reservationInfo.size());
        Assertions.assertEquals(9, reservationInfo.get(0).getId());
        Assertions.assertEquals(10, reservationInfo.get(1).getId());
        Assertions.assertEquals(11, reservationInfo.get(2).getId());
    }

    @Test
    public void 발송일_기준_19시_이후_내역은_조회하지_않는다() {
        LocalDateTime toDate = LocalDateTime.of(2024, 6, 5, 19, 0, 0);
        LocalDateTime fromDate = toDate.minusDays(1);

        List<ReservationInfo> reservationInfo = reservationInfoRepository.findUpdatedReservationInfos(fromDate, toDate);
        Assertions.assertEquals(0, reservationInfo.size());
    }

}
