package com.dj.ckw.userservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

/**
 * Conditional JDBC Auditing and Scheduling configuration.
 * Only enables when a datasource is available.
 * In degraded mode (no database), this configuration is skipped.
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.url")
@EnableJdbcAuditing
public class DatabaseConfig {
  // Configuration will only be applied if spring.datasource.url is present
}

