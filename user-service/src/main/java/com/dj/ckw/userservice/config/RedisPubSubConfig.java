package com.dj.ckw.userservice.config;

import com.dj.ckw.userservice.service.UserEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redis Pub/Sub configuration.
 * Only loads when Redis is available and explicitly enabled.
 * In degraded mode (no Redis), event publishing is disabled.
 */
@Configuration
@ConditionalOnProperty(name = "spring.data.redis.host")
public class RedisPubSubConfig {

  private static final Logger log = LoggerFactory.getLogger(RedisPubSubConfig.class);
  public static final String USER_EVENTS_TOPIC = "user-events";

  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    log.info("Initializing Redis Pub/Sub message listener");
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(listenerAdapter, new ChannelTopic(USER_EVENTS_TOPIC));
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(UserEventListener receiver) {
    return new MessageListenerAdapter(receiver, "onMessage");
  }
}

