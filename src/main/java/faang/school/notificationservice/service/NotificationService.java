package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {

    void sendNotification(UserDto userDto, String message);

    UserDto.PreferredContact getPreferredContact();
}