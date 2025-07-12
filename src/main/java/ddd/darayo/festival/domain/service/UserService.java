package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.constant.AuthProviderType;
import ddd.darayo.festival.domain.entity.User;
import ddd.darayo.festival.domain.repository.UserRepository;
import ddd.darayo.festival.infra.jwt.JwtService;
import ddd.darayo.festival.presentation.user.exchanges.UserLoginRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserLoginRes login(String deviceId) {
        User user = userRepository.findByProviderUserId(deviceId)
                .orElseGet(() -> userRepository.save(User.builder()
                        .provider(AuthProviderType.DEVICE)
                        .providerUserId(deviceId)
                        .build()));
        String token = jwtService.generateToken(user);

        return UserLoginRes.builder()
                .token(token)
                .build();
    }

    public void updatePushPermission(Long userId, boolean permissionEnabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(); // 인터셉터에서 이미 검증했지만, 혹시 모르니 추가

        user.updateAlarmPermission(permissionEnabled);
    }
}