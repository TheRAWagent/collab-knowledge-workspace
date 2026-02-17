package com.dj.ckw.authservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Conditional JPA Auditing configuration.
 * Only enables JPA Auditing when a datasource is available.
 * In degraded mode (no database), this configuration is skipped.
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.url")
@EnableJpaAuditing
public class DatabaseConfig {
  // Configuration will only be applied if spring.datasource.url is present
}


