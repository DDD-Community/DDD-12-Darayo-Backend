package ddd.darayo.festival.presentation.performance.exchanges;


import com.fasterxml.jackson.annotation.JsonInclude;
import ddd.darayo.festival.domain.constant.ReservationType;
import java.time.LocalDateTime;


public record EditReservationInfoReq(

        Long id,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime openDateTime,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime closeDateTime,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        ReservationType type,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String ticketURL,
        String remark
) { }
