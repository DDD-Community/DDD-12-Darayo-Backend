package ddd.darayo.festival.domain.dto;

import java.time.LocalDateTime;

import ddd.darayo.festival.domain.constant.ReservationType;

public record ReservationInfoContentDTO(
    LocalDateTime openDateTime,
    LocalDateTime closeDateTime,
    ReservationType type,
    String ticketURL,
    String remark
) {}