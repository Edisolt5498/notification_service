package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener {

    private final List<NotificationService> notificationServices;

    protected void sendNotification(String message, UserDto userDto) {
        notificationServices.stream().filter(notificationService
                        -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .ifPresent(notificationService
                        -> notificationService.sendNotification(userDto, message));
    }

    protected <EventType> EventType getDto(Message message, Class<EventType> eventTypeClass) {
        String body = Arrays.toString(message.getBody());

        try {
            return new ObjectMapper().readValue(body, eventTypeClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}