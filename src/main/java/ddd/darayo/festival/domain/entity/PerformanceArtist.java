package ddd.darayo.festival.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate performanceDate;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Artist artist;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Performance performance;

    public PerformanceArtist(LocalDate performanceDate, Artist artist) {
        this.performanceDate = performanceDate;
        this.artist = artist;
    }
}
