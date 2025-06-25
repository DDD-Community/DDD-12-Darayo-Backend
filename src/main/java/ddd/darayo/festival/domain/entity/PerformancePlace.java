package ddd.darayo.festival.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PerformancePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 512)
    private String address;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "place")
    private List<PerformanceHall> halls = new ArrayList<>();

    public PerformancePlace(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public PerformancePlace(Long id) {
        this.id = id;
    }

    public void addHall(PerformanceHall hall) {
        halls.add(hall);
        hall.setPlace(this);
    }

    public void update(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
