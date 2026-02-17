package com.dj.ckw.authservice.config;

import com.dj.ckw.authservice.grpc.UserIdentityConfirmationRequest;
import com.dj.ckw.authservice.grpc.UserIdentityConfirmationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fallback wrapper for UserIdentityConfirmationServiceBlockingV2Stub.
 * Used when the gRPC user-service is not available.
 * Returns a default response that allows the application to start without external service connectivity.
 */
public class NoOpUserIdentityStub implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(NoOpUserIdentityStub.class);

    /**
     * Confirms user identity without contacting the remote service.
     * In degraded mode, this always returns unconfirmed to prevent user creation
     * until the user-service is available.
     */
    public UserIdentityConfirmationResponse confirmUserIdentity(UserIdentityConfirmationRequest request) {
        log.warn("User-service is not available. Running in degraded mode. User identity confirmation disabled for: {}",
                request.getEmail());
        // Return unconfirmed to prevent user creation in degraded mode
        return UserIdentityConfirmationResponse.newBuilder()
                .setIsConfirmed(false)
                .build();
    }

    @Override
    public void close() throws Exception {
        // No-op close
    }
}


