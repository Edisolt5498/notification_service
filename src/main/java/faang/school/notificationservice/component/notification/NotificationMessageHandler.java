package faang.school.notificationservice.component.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationMessageHandler { // Тебе не нужно вручную извлекать message.getBody() и message.getChannel(). Адаптер сделает это за тебя.

    public void handleMessage(String message, String channel) {
        log.info("Received message: {} from channel: {}", message, channel);
    }

    /*
    public class MessagePayload {
        private String content;
        private long timestamp;

        // Геттеры, сеттеры
    }

    public void handleMessage(MessagePayload payload, String channel) { // Для моего кода с PostMessage
        log.info("Received payload: {} from channel: {}", payload, channel);
    }
     */
}