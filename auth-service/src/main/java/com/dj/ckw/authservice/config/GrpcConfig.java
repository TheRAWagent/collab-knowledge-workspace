package com.dj.ckw.authservice.config;

import com.dj.ckw.authservice.grpc.UserIdentityConfirmationServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfig {

    @Bean
    UserIdentityConfirmationServiceGrpc.UserIdentityConfirmationServiceBlockingV2Stub stub(GrpcChannelFactory channels) {
        return UserIdentityConfirmationServiceGrpc.newBlockingV2Stub(channels.createChannel("user-service"));
    }
}
