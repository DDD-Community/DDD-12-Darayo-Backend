package ddd.darayo.festival.domain.entity;

import ddd.darayo.festival.domain.constant.ParticipationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TimetableArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationType participationType;

    @Setter
    @ManyToOne
    private Artist artist;

    @Setter
    @ManyToOne
    private Timetable timetable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimetableArtist that)) return false;
        return Objects.equals(id, that.id) &&
                participationType == that.participationType &&
                Objects.equals(artist, that.artist) &&
                Objects.equals(timetable, that.timetable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, participationType, artist, timetable);
    }
}
