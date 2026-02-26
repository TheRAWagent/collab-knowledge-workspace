package com.dj.ckw.authservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redis Pub/Sub configuration for auth-service.
 * Only loads when Redis is configured. In degraded mode
 * (no Redis), pub/sub and key-value operations are disabled.
 */
@Configuration
@ConditionalOnProperty(name = "spring.data.redis.host")
public class RedisPubSubConfig {

  private static final Logger log = LoggerFactory.getLogger(RedisPubSubConfig.class);

  public static final String USER_EVENTS_TOPIC = "user-events";
  public static final String PASSWORD_RESET_KEY_PREFIX = "password_reset:";

  @Bean
  public RedisMessageListenerContainer redisListenerContainer(RedisConnectionFactory connectionFactory) {
    log.info("Initializing Redis message listener container (auth-service)");
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    return container;
  }

  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
    return new StringRedisTemplate(connectionFactory);
  }
}
