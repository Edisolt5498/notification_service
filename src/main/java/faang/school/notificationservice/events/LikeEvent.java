package faang.school.notificationservice.events;

public record LikeEvent(long postAuthorId, long likeAuthorId, long postId) {
}
