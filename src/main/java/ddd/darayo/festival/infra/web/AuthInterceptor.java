package ddd.darayo.festival.infra.web;

import ddd.darayo.festival.domain.exception.constant.AuthError;
import ddd.darayo.festival.domain.repository.UserRepository;
import ddd.darayo.festival.infra.jwt.JwtService;
import ddd.darayo.festival.presentation.common.exception.APIException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static ddd.darayo.festival.domain.exception.constant.UserError.NOT_FOUND_USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }

        String token = authHeader.substring(7);

        try {
            // JWT 토큰에서 userId 추출
            Long userId = jwtService.getUserId(token);
            
            // 사용자 존재 여부 확인
            if (userRepository.findById(userId).isEmpty()) {
                throw NOT_FOUND_USER.toException();  // 유효하지 않은 User
            }
            
            request.setAttribute("userId", userId);
        } catch (JwtException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            throw new APIException(AuthError.AUTH_FAIL, HttpStatus.BAD_REQUEST);
        }

        return true;
    }
}
