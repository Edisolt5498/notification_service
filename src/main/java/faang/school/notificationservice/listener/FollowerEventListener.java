package faang.school.notificationservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener {
    @KafkaListener(topics = "follower_topic", groupId = "user-service-group-ban")
    public void consume(String message) {
        System.out.println("Received message: " + message);
        // Ваша логика обработки сообщения
    }
}
