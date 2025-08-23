package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.ReservationInfo;
import ddd.darayo.festival.domain.dto.ReservationInfoContentDTO;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ReservationInfoMapper {
    PerformanceDetailRes.ReservationInfoDetailRes toReservationDetailRes(ReservationInfo reservationInfo);

    @Mapping(target = "reservationInfoId", source = "id")
    UserGetPerformanceInfo.ReservationInfoDetailRes toUserReservationDetailRes(ReservationInfo reservationInfo);

    @Mapping(target = "openTimeModifiedAt", source = "now")
    ReservationInfo toReservationInfo(ReservationInfoContentDTO dto, LocalDateTime now);
}
