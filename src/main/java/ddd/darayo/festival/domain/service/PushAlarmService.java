package ddd.darayo.festival.domain.service;

import java.util.List;

public interface PushAlarmService {
    record MessageDTO (
        String title,
        String message,
        List<String> receivers
    ){}
    void sendAlarm(MessageDTO messageDTO);
}
