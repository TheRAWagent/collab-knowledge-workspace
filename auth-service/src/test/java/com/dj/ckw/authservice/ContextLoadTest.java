package com.dj.ckw.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ContextLoadTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  void contextLoads() {
    assertThat(applicationContext).isNotNull();
  }

  @Test
  void objectMapperBeanExists() {
    assertThat(applicationContext.containsBean("objectMapper")).isTrue();
    assertThat(applicationContext.getBean(ObjectMapper.class)).isNotNull();
  }

  @Test
  void cacheManagerBeanExists() {
    assertThat(applicationContext.containsBean("cacheManager")).isTrue();
    assertThat(applicationContext.getBean(CacheManager.class)).isNotNull();
  }
}
