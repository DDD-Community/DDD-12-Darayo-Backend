package ddd.darayo.festival.infra.alarm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
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

        return MulticastMessage.builder()
                .setNotification(notification)
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