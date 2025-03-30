package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowerEventListener {
    private final String followerTopic = "${topic.follower-topic}";
    private final String kafkaConsumerGroupId = "${spring.data.kafka.consumer.group-id}";


    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;

    @KafkaListener(topics = followerTopic, groupId = kafkaConsumerGroupId, properties = {"spring.json.value.default.type=faang.school.notificationservice.event"})
    public void consume(String message) throws IOException {

        try {
            System.out.println("Received message: " + message);
            // Ваша логика обработки сообщения

            FollowerEvent event = objectMapper.readValue(message, FollowerEvent.class);

            System.out.println("Received event: " + event);
            // Ваша логика обработки сообщения

            UserNotificationDto user = userServiceClient.getUserNotificationDto(event.getFolloweeId());

            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == user.getPreference())
                    .findFirst()
                    .ifPresent(service -> service.send(user, "You've got a new follower!"));
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
}
