package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.LikeEvent;
import faang.school.notificationservice.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {

        var userDto = getUser(event.likeAuthorId());

        return messageSource.getMessage("like.created", new Object[]{userDto.getUsername()}, Locale.getDefault());
    }

    private UserDto getUser(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (Exception ex) {
            throw new UserNotFoundException("User with id #%d is not found".formatted(userId), ex);
        }
    }
}
