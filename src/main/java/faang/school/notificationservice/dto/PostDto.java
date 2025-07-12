package faang.school.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private String title;
    private Long id;
    private Long authorId;
}
