package com.dj.ckw.apigateway.service;

import com.dj.ckw.apigateway.grpc.PageAccessRequest;
import com.dj.ckw.apigateway.grpc.PageAccessResponse;
import com.dj.ckw.apigateway.grpc.WorkspaceServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.micrometer.core.instrument.binder.grpc.ObservationGrpcClientInterceptor;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WorkspaceServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(WorkspaceServiceGrpcClient.class);
    private final WorkspaceServiceGrpc.WorkspaceServiceStub asyncStub;

    public WorkspaceServiceGrpcClient(
            @Value("${workspace.service.address:localhost}") String serverAddress,
            @Value("${workspace.service.grpc.port:9090}") int serverPort,
            ObservationRegistry observationRegistry) {
        log.info("Connecting to Workspace service grpc server at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().intercept(new ObservationGrpcClientInterceptor(observationRegistry)).build();
        this.asyncStub = WorkspaceServiceGrpc.newStub(channel);
    }

    public Mono<PageAccessResponse> checkPageAccess(PageAccessRequest request) {
        return Mono.create(sink -> {
            asyncStub.checkPageAccess(request, new StreamObserver<>() {
                @Override
                public void onNext(PageAccessResponse value) {
                    sink.success(value);
                }

                @Override
                public void onError(Throwable t) {
                    log.error("Error from Workspace gRPC Service", t);
                    sink.error(t);
                }

                @Override
                public void onCompleted() {
                }
            });
        });
    }
}
