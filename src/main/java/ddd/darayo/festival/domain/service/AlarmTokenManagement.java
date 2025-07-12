package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.UserAlarmToken;
import ddd.darayo.festival.domain.exception.constant.AlarmError;
import ddd.darayo.festival.domain.repository.UserAlarmTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlarmTokenManagement {
    private final UserAlarmTokenRepository userAlarmTokenRepository;

    @Transactional
    public void deleteToken(Long userId, String token) {
        UserAlarmToken userToken = userAlarmTokenRepository.findByUserIdAndToken(userId, token)
                .orElseThrow(AlarmError.ALARM_TOKEN_NOT_EXIST::toException);

        userAlarmTokenRepository.delete(userToken);
    }

    @Transactional
    public void upsertToken(Long userId, String token, LocalDateTime now) {
        Optional<UserAlarmToken> optionalToken = userAlarmTokenRepository.findByUserIdAndToken(userId, token);

        if (optionalToken.isPresent()) {
            UserAlarmToken userToken = optionalToken.get();
            userToken.refresh(now);
        } else {
            UserAlarmToken userToken = new UserAlarmToken(userId, token, now);
            userAlarmTokenRepository.save(userToken);
        }
    }
}
