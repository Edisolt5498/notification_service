package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscribeEventListener extends AbstractEventListener implements MessageListener {

    public SubscribeEventListener(List<NotificationService> notificationServices) {
        super(notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] channel = message.getChannel();
        byte[] body = message.getBody();

        sendNotification("", new UserDto());
    }
}
