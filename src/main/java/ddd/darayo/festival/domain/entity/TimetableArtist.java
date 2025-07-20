package ddd.darayo.festival.domain.entity;

import ddd.darayo.festival.domain.constant.ParticipationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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
}
