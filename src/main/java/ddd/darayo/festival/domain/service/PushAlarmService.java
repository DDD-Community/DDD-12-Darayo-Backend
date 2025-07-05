package ddd.darayo.festival.domain.service;

import java.util.List;
import java.util.Map;

public interface PushAlarmService {
    record MessageDTO (
        String title,
        String message,
        List<String> receivers,
        Map<String, String> payload
    ){}
    void sendAlarm(MessageDTO messageDTO);
}