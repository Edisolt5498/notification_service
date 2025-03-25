package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.event.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FollowerEventListener {
    private final String followerTopic = "${topic.follower-topic}";
    private final String kafkaConsumerGroupId = "${spring.data.kafka.consumer.group-id}";

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = followerTopic, groupId = kafkaConsumerGroupId, properties = {"spring.json.value.default.type=faang.school.notificationservice.event"})
    public void consume(String message) throws IOException {
        System.out.println("Received message: " + message);
        // Ваша логика обработки сообщения

        FollowerEvent event = objectMapper.readValue(message, FollowerEvent.class);

        System.out.println("Received event: " + event);
        // Ваша логика обработки сообщения
    }
}
