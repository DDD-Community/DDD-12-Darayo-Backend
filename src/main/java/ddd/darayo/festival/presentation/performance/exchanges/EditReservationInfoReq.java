package ddd.darayo.festival.presentation.performance.exchanges;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.EditReservationInfoCommand;



public record EditReservationInfoReq(
        Long id,
        @JsonUnwrapped
        EditReservationInfoCommand command
) { }
