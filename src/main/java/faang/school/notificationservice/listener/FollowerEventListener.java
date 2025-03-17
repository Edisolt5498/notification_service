package faang.school.notificationservice.listener;

import org.springframework.kafka.annotation.KafkaListener;

public class FollowerEventListener {
    @KafkaListener(topics = "follower_topic", groupId = "1")
    public void consume(String message) {
        System.out.println("Received message: " + message);
        // Ваша логика обработки сообщения
    }
}
