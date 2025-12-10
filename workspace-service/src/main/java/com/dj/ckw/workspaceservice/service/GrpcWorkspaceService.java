package com.dj.ckw.workspaceservice.service;

import com.dj.ckw.workspaceservice.enums.WorkspaceMemberRole;
import com.dj.ckw.workspaceservice.grpc.PageAccessRequest;
import com.dj.ckw.workspaceservice.grpc.PageAccessResponse;
import com.dj.ckw.workspaceservice.grpc.WorkspaceServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GrpcWorkspaceService extends WorkspaceServiceGrpc.WorkspaceServiceImplBase {

    private final WorkspaceService workspaceService;

    public GrpcWorkspaceService(WorkspaceService workspaceService) {
        super();
        this.workspaceService = workspaceService;
    }

    @Override
    public void checkPageAccess(PageAccessRequest request, StreamObserver<PageAccessResponse> responseStreamObserver) {
        Optional<WorkspaceMemberRole> role = workspaceService.getWorkspaceMemberRole(request.getWorkspaceId(), request.getUserId());
        if (role.isEmpty()) {
            responseStreamObserver.onNext(PageAccessResponse.newBuilder().setHasAccess(false).build());
            responseStreamObserver.onCompleted();
        }
        responseStreamObserver.onNext(PageAccessResponse.newBuilder().setHasAccess(true).setWorkspaceMemberRole(role.get()).build());
        responseStreamObserver
                .onCompleted();
    }
}
