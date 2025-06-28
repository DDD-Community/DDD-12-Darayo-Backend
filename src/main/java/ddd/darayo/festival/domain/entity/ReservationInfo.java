package ddd.darayo.festival.domain.entity;

import ddd.darayo.festival.domain.constant.ReservationType;
import ddd.darayo.festival.presentation.performance.exchanges.EditReservationInfoReq;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime openDateTime;

    @Column(nullable = false)
    private LocalDateTime closeDateTime;

    @Column(length = 512)
    private String ticketURL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationType type;

    @Lob
    @Column
    private String remark;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Performance performance;

    public ReservationInfo(LocalDateTime openDateTime, LocalDateTime closeDateTime, 
                          String ticketURL, ReservationType type, String remark) {
        this.openDateTime = openDateTime;
        this.closeDateTime = closeDateTime;
        this.ticketURL = ticketURL;
        this.type = type;
        this.remark = remark;
    }

    public void updateWith(EditReservationInfoReq req) {
        if (req.openDateTime() != null) {
            this.openDateTime = req.openDateTime();
        }
        if (req.closeDateTime() != null) {
            this.closeDateTime = req.closeDateTime();
        }
        if (req.ticketURL() != null) {
            this.ticketURL = req.ticketURL();
        }
        if (req.type() != null) {
            this.type = req.type();
        }
        if (req.remark() != null) {
            this.remark = req.remark();
        }
    }
}