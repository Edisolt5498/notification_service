package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.PostDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PostEventListener extends AbstractEventListener implements MessageListener {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;
    private final static String messageCode = "new.post";

    public PostEventListener(List<NotificationService> notificationServices, UserServiceClient userServiceClient, MessageSource messageSource) {
        super(notificationServices);
        this.userServiceClient = userServiceClient;
        this.messageSource = messageSource;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostDto postDto = getDto(message, PostDto.class);

        String postTitle = postDto.getTitle();
        Long postAuthorId = postDto.getAuthorId();

        String authorName = userServiceClient.getUser(postAuthorId).getUsername();
        List<UserDto> userSubscribers = userServiceClient.getUserSubscribers(postAuthorId);

        Map<Locale, String> localeMessagesMap = new HashMap<>();
        userSubscribers
                .stream()
                .map(UserDto::getLocale)
                .distinct().forEach(locale -> localeMessagesMap.put(
                        locale,
                        messageSource.getMessage(messageCode, new Object[]{authorName, postTitle}, locale)
                        ));

        userSubscribers.forEach(userDto -> sendNotification(localeMessagesMap.get(userDto.getLocale()), userDto));
    }
}