package ddd.darayo.festival.domain.entity;

import ddd.darayo.festival.domain.constant.AuthProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String providerUserId;

    @Column
    @Enumerated(EnumType.STRING)
    private AuthProviderType provider;

    @Column
    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAlarmAllowed = false;

    public void updateAlarmPermission(boolean allowed) {
        this.isAlarmAllowed = allowed;
    }
}
