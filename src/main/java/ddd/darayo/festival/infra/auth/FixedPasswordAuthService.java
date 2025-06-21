package ddd.darayo.festival.infra.auth;

import ddd.darayo.festival.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FixedPasswordAuthService implements AuthService {
    @Value("${auth.password}")
    private String adminPassword;

    @Override
    public boolean authenticate(String password) {
        return password.equals(adminPassword);
    }
}
