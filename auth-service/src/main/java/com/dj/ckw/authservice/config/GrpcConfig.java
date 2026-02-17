package com.dj.ckw.authservice.config;

import com.dj.ckw.authservice.grpc.UserIdentityConfirmationServiceGrpc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * gRPC configuration with fallback support.
 * Attempts to create a stub for user-service only if explicitly enabled.
 * This allows the application to start in degraded mode without gRPC connectivity.
 */
@Configuration
public class GrpcConfig {
    private static final Logger log = LoggerFactory.getLogger(GrpcConfig.class);

    @Bean
    @ConditionalOnProperty(
        name = "app.grpc.enabled",
        havingValue = "true",
        matchIfMissing = false
    )
    UserIdentityConfirmationServiceGrpc.UserIdentityConfirmationServiceBlockingV2Stub stub(
            GrpcChannelFactory channels) {
        try {
            log.info("Creating gRPC stub for user-service");
            var stub = UserIdentityConfirmationServiceGrpc.newBlockingV2Stub(
                    channels.createChannel("user-service"));
            log.info("gRPC stub for user-service created successfully");
            return stub;
        } catch (Exception e) {
            log.warn("Failed to create gRPC stub for user-service. Error: {}", e.getMessage());
            throw new RuntimeException("gRPC initialization failed and is required", e);
        }
    }
}
