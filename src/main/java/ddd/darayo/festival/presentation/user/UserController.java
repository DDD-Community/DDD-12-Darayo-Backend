package ddd.darayo.festival.presentation.user;

import ddd.darayo.festival.domain.user.service.UserService;
import ddd.darayo.festival.presentation.user.exchanges.UserLoginReq;
import ddd.darayo.festival.presentation.user.exchanges.UserLoginRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginRes> login(@RequestBody UserLoginReq req) {
        return ResponseEntity.ok(userService.login(req.getDeviceId()));
    }
}
