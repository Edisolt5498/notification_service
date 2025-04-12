package faang.school.notificationservice.component.notification;

import faang.school.notificationservice.notification.PostNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener implements MessageListener {

    private final Jackson2JsonRedisSerializer<PostNotification> serializer =
            new Jackson2JsonRedisSerializer<>(PostNotification.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostNotification notification = serializer.deserialize(message.getBody());
            log.info("Received notification: {}", notification);

            processNotification(notification);
        } catch (Exception e) {
            log.error("Failed to process message: {}", new String(message.getBody()), e);
        }
    }

    private void processNotification(PostNotification notification) {
        log.info("Processing notification for post: \n{} \n" +
                        "Author id: {}\n" +
                        "Project id: {}\n" +
                        "Content: {}",
                notification.getPostId(),
                notification.getAuthorId(),
                notification.getProjectId(),
                notification.getContent()
        );
    }
}
