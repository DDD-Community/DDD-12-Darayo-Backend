package ddd.darayo.festival.domain.entity;

import ddd.darayo.festival.domain.constant.ParticipationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
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
    private PerformanceArtist artist;

    @Setter
    @ManyToOne
    private Timetable timetable;
}
