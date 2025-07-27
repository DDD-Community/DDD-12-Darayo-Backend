package ddd.darayo.festival.domain.entity;

import ddd.darayo.festival.domain.dto.EditPerformanceDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    @Column(nullable = true, length = 512)
    private String posterUrl;

    @Lob
    @Column(nullable = true, columnDefinition = "TEXT")
    private String banGoods;

    @Lob
    @Column(nullable = true, columnDefinition = "TEXT")
    private String transportationInfo;

    @Lob
    @Column(nullable = true)
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    private PerformancePlace place;

    public Performance(Long performanceId) {
        this.id = performanceId;
    }
    // TODO: 추후에 각각의 도메인으로 분리하면 좋아 보임.
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "performance")
    @Builder.Default
    private Set<Timetable> timetables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "performance")
    @Builder.Default
    private Set<ReservationInfo> reservationInfos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "performance")
    @Builder.Default
    private Set<PerformanceURL> urls = new HashSet<>();

    public void addTimetable(Timetable timetable) {
        this.timetables.add(timetable);
        timetable.setPerformance(this);
    }

    public void addReservationInfo(ReservationInfo reservationInfo) {
        this.reservationInfos.add(reservationInfo);
        reservationInfo.setPerformance(this);
    }

    public void addUrl(PerformanceURL performanceURL) {
        this.urls.add(performanceURL);
        performanceURL.setPerformance(this);
    }

    public void update(EditPerformanceDTO dto) {
        this.name = dto.name();
        this.startDate = dto.startDate();
        this.endDate = dto.endDate();
        this.banGoods = dto.banGoods();
        this.transportationInfo = dto.transportationInfo();
        this.remark = dto.remark();
        this.posterUrl = dto.posterUrl();
    }
}
