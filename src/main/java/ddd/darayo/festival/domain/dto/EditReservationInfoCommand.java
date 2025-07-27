package ddd.darayo.festival.domain.dto;

import ddd.darayo.festival.domain.constant.ReservationType;

import java.time.LocalDateTime;

public record EditReservationInfoCommand(
        LocalDateTime openDateTime,
        LocalDateTime closeDateTime,
        ReservationType type,
        String ticketURL,
        String remark
) { }
