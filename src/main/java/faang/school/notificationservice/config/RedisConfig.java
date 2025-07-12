package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.PostEventListener;
import faang.school.notificationservice.listener.SubscribeEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,

                                            MessageListenerAdapter postEventMessageAdapter,
                                            MessageListenerAdapter subscribeEventMessageAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(postEventMessageAdapter, new ChannelTopic("postEvent"));
        container.addMessageListener(subscribeEventMessageAdapter, new ChannelTopic("subscribeEvent"));

        return container;
    }

    @Bean
    MessageListenerAdapter postEventMessageAdapter(PostEventListener postEventListener) {
        return new MessageListenerAdapter(postEventListener);
    }

    @Bean
    MessageListenerAdapter subscribeEventMessageAdapter(SubscribeEventListener subscribeEventListener) {
        return new MessageListenerAdapter(subscribeEventListener);
    }
}
