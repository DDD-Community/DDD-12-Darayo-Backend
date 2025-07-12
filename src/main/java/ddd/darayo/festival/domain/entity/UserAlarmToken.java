package ddd.darayo.festival.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserAlarmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=512, nullable=false)
    private String alarmToken;

    @Column
    @Builder.Default
    private Boolean isValid = true;

    @Column
    private LocalDateTime expiredAt;

    @Column
    private LocalDateTime updatedAt;

    @Column(nullable=false)
    private Long userId;

    public void deactivate(LocalDateTime expiredAt) {
        isValid = false;
        this.expiredAt = expiredAt;
    }

    public void refresh(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserAlarmToken(Long userId, String alarmToken, LocalDateTime createdAt) {
        this.userId = userId;
        this.alarmToken = alarmToken;
        this.updatedAt = createdAt;
    }
}
