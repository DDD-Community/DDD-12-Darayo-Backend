package ddd.darayo.festival.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate performanceDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(length = 512)
    private String performanceHall;

    @Setter
    @ManyToOne
    private Performance performance;

    public Timetable(LocalDate performanceDate, LocalTime startTime, LocalTime endTime, String performanceHall) {
        this.performanceDate = performanceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.performanceHall = performanceHall;
    }
}
