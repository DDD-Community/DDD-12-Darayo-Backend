package ddd.darayo.festival.domain.service.mapper;

import ddd.darayo.festival.domain.entity.ReservationInfo;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationInfoMapper {
    PerformanceDetailRes.ReservationInfoDetailRes toReservationDetailRes(ReservationInfo reservationInfo);

    ReservationInfo toReservationEntity(SavePerformanceReq.ReservationInfoDTO dto);
}
