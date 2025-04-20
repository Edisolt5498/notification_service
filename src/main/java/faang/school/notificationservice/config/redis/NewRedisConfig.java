package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.component.notification.NewNotificationListener;
import faang.school.notificationservice.component.notification.NotificationMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class NewRedisConfig {
    /*@Bean // Для паблишера
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        //template.setHashKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        template.afterPropertiesSet();
        return template;
    }*/

    @Bean // Используем или это, или ниже
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            NewNotificationListener newNotificationListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(newNotificationListener, new ChannelTopic("post-notifications"));
        return container;
    }

    /******************************************************************************************************************/
    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("my-channel");
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(NotificationMessageHandler handler) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(handler); // Можно указать, какой метод вызывать new MessageListenerAdapter(handler, "customMethodName");
        adapter.setSerializer(new StringRedisSerializer());
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, topic());

        return container;
    }

    /******************************************************************************************************************/
    private final NewNotificationListener newNotificationListener;

    @Autowired
    public NewRedisConfig(NewNotificationListener newNotificationListener) {
        this.newNotificationListener = newNotificationListener;
    }

    @Bean
    public RedisMessageListenerContainer redisListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(newNotificationListener);
        return container;
    }

    /*
    @Configuration
public class RedisConfig {

    private final List<NotificationListener> listeners;

    @Autowired
    public RedisConfig(List<NotificationListener> listeners) {
        this.listeners = listeners;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // Регистрируем всех слушателей
        for (NotificationListener listener : listeners) {
            MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
            adapter.setSerializer(new StringRedisSerializer());
            container.addMessageListener(adapter, new ChannelTopic(listener.getChannel()));
        }

        return container;
    }
}
     */
}
