package ddd.darayo.festival.domain.entity;

import ddd.darayo.festival.domain.constant.URLType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
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

    public void update(String url, URLType type) {
        this.url = url;
        this.type = type;
    }
}
