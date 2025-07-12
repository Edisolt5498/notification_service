package faang.school.notificationservice.listener;

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
    private final MessageSource messageSource;
    private final static String messageCode = "new.post";

    public PostEventListener(List<NotificationService> notificationServices, MessageSource messageSource) {
        super(notificationServices);
        this.messageSource = messageSource;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostDto postDto = getDto(message, PostDto.class);

        String postTitle = postDto.getTitle();
        Long postAuthorId = postDto.getAuthorId();

        String authorName = new UserDto().getUsername();//todo /users/{postAuthorId}
        List<UserDto> subscribers = new ArrayList<>(); // todo /users/{postAuthorId}/subscribers from UserService

        Map<Locale, String> localeMessagesMap = new HashMap<>();
        subscribers
                .stream()
                .map(UserDto::getLocale)
                .distinct().forEach(locale -> localeMessagesMap.put(
                        locale,
                        messageSource.getMessage(messageCode, new Object[]{authorName, postTitle}, locale)
                        ));

        subscribers.forEach(userDto -> sendNotification(localeMessagesMap.get(userDto.getLocale()), userDto));
    }
}