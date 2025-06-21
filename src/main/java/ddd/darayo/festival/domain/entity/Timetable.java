package ddd.darayo.festival.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate performanceDate;

    @Column(nullable = true)
    private LocalTime startTime;

    @Column(nullable = true)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private PerformanceHall hall;

    @Setter
    @ManyToOne
    private Performance performance;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "timetable")
    private List<TimetableArtist> artists = new ArrayList<>();

    public Timetable(LocalDate performanceDate, LocalTime startTime, LocalTime endTime, Long hallId) {
        this.performanceDate = performanceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hall = new PerformanceHall(hallId);
    }

    public void addArtist(TimetableArtist artist) {
        this.artists.add(artist);
        artist.setTimetable(this);
    }

    public void update(LocalDate performanceDate, LocalTime startTime, LocalTime endTime, PerformanceHall hall) {
        this.performanceDate = performanceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hall = hall;
    }
}
