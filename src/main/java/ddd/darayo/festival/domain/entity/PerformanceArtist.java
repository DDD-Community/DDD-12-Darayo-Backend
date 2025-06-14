package ddd.darayo.festival.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Artist artist;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Performance performance;

    public PerformanceArtist(Artist artist) {
        this.artist = artist;
    }

    public PerformanceArtist(Long artistId) {
        this.artist = new Artist(artistId);
    }

    public PerformanceArtist(Long artistId, Long performanceId) {
        this.artist = new Artist(artistId);
        this.performance = new Performance(performanceId);
    }
}
