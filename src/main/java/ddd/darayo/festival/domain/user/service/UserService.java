package ddd.darayo.festival.domain.user.service;

import ddd.darayo.festival.domain.user.entity.Provider;
import ddd.darayo.festival.domain.user.entity.User;
import ddd.darayo.festival.domain.user.repository.UserRepository;
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
        String token = jwtService.generateToken(deviceId);

        userRepository.save(User.builder()
                .provider(Provider.DEVICE)      // todo : apple 로그인 구현되면 파라미터에 추가하고, 해당 부분도 바꿔줘야함
                .providerUserId(token)
                .build());

        return UserLoginRes.builder()
                .token(token)
                .build();
    }
}