package ddd.darayo.festival.presentation.user;

import ddd.darayo.festival.domain.service.UserService;
import ddd.darayo.festival.presentation.common.BaseResponse;
import ddd.darayo.festival.presentation.user.exchanges.PushPermissionReq;
import ddd.darayo.festival.presentation.user.exchanges.UserLoginReq;
import ddd.darayo.festival.presentation.user.exchanges.UserLoginRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<UserLoginRes>> login(@RequestBody UserLoginReq req) {
        return ResponseEntity.ok(BaseResponse.success(userService.login(req.getDeviceId())));
    }

    @PostMapping("/push-permission")
    public ResponseEntity<BaseResponse<Void>> updatePushPermission(
            @RequestAttribute("providerUserId") String providerUserId,
            @RequestBody PushPermissionReq req
    ) {
        userService.updatePushPermission(providerUserId, req.getPermissionEnabled());
        return ResponseEntity.ok(BaseResponse.success());
    }
}

