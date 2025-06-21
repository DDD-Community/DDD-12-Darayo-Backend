package ddd.darayo.festival.domain.entity;

import ddd.darayo.festival.domain.constant.URLType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceURL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String url;

    @Column(nullable = false)
    private URLType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Performance performance;
}
