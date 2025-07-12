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
            @RequestBody TokenEnrollReq request
    ) {
        Long dummyUserId = 1L;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        alarmTokenManagement.upsertToken(dummyUserId, request.token(), now);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<BaseResponse<Void>> deleteAlarm(
            @PathVariable String token
    ) {
        Long dummyUserId = 1L;
        alarmTokenManagement.deleteToken(dummyUserId, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
