package ddd.darayo.festival.presentation.alarm;

import ddd.darayo.festival.domain.service.AlarmTokenManagement;
import ddd.darayo.festival.presentation.alarm.exchanges.TokenEnrollReq;
import ddd.darayo.festival.presentation.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/v1/user/alarm")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmTokenManagement alarmTokenManagement;

    @PutMapping
    public ResponseEntity<BaseResponse<Void>> upsertAlarm(
            @RequestAttribute("userId") Long userId,
            @RequestBody TokenEnrollReq request
    ) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        alarmTokenManagement.upsertToken(userId, request.token(), now);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<BaseResponse<Void>> deleteAlarm(
            @RequestAttribute("userId") Long userId,
            @PathVariable String token
    ) {
        alarmTokenManagement.deleteToken(userId, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
