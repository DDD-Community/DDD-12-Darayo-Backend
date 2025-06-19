package ddd.darayo.festival.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private PerformancePlace place;

    public PerformanceHall(Long id) {
        this.id = id;
    }

    public PerformanceHall(String name) {
        this.name = name;
    }
}
