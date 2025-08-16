package ddd.darayo.festival.infra.alarm;

import com.google.firebase.messaging.*;
import ddd.darayo.festival.domain.constant.AlarmConstant;
import ddd.darayo.festival.domain.exception.constant.AlarmError;
import ddd.darayo.festival.domain.service.PushAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
public class FirebaseCloudMessagingService implements PushAlarmService {
    
    @Override
    public void sendAlarm(MessageDTO messageDTO) {
        try {
            if (messageDTO.receivers().isEmpty()) {
                log.info("FCM 토큰 목록이 비어있습니다.");
                return;
            }
            
            MulticastMessage multicastMessage = buildMulticastMessage(messageDTO);
            BatchResponse batchResponse = FirebaseMessaging.getInstance()
                    .sendEachForMulticast(multicastMessage);
            
            handleBatchResponse(batchResponse, messageDTO.receivers());
            
        } catch (Exception e) {
            log.error("FCM 메시지 전송 중 오류 발생: {}", e.getMessage(), e);
            throw AlarmError.ALARM_SEND_FAILED.toException();
        }
    }
    
    private MulticastMessage buildMulticastMessage(MessageDTO messageDTO) {
        Notification notification = Notification.builder()
                .setTitle(messageDTO.title())
                .setBody(messageDTO.message())
                .build();

        ApsAlert apsAlert = ApsAlert.builder()
                .setTitle(messageDTO.title())
                .setBody(messageDTO.message())
                .build();

        Aps aps = Aps.builder()
                .setAlert(apsAlert)
                .setSound("default")   // 선택
                .setBadge(1)           // 선택
                .build();

        ApnsConfig apns = ApnsConfig.builder()
                .putHeader("apns-push-type", AlarmConstant.APNS_PUSH_TYPE)
                .putHeader("apns-priority", AlarmConstant.APNS_PRIORITY)
                .putHeader("apns-topic", AlarmConstant.BUNDLE_ID)
                .setAps(aps)
                .putAllHeaders(messageDTO.payload())
                .build();

        return MulticastMessage.builder()
                .setNotification(notification)
                .setApnsConfig(apns)
                .addAllTokens(messageDTO.receivers())
                .putAllData(messageDTO.payload())
                .build();
    }
    
    private void handleBatchResponse(BatchResponse batchResponse, List<String> tokens) {
        if (batchResponse.getFailureCount() > 0) {
            List<SendResponse> responses = batchResponse.getResponses();
            IntStream.range(0, responses.size())
                    .forEach(i -> {
                        SendResponse response = responses.get(i);
                        if (!response.isSuccessful()) {
                            String token = tokens.get(i);
                            String errorCode = response.getException() != null 
                                    ? response.getException().getErrorCode().toString() 
                                    : "UNKNOWN";
                            log.warn("FCM 토큰 전송 실패 - Token: {}, Error: {}", 
                                    token, errorCode);
                        }
                    });
        }
        
        log.info("FCM 전송 완료 - 성공: {}, 실패: {}", 
                batchResponse.getSuccessCount(), 
                batchResponse.getFailureCount());
    }
} 