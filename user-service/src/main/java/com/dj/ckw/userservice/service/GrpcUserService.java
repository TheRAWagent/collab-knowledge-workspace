package com.dj.ckw.userservice.service;

import com.dj.ckw.userservice.grpc.UserIdentityConfirmationRequest;
import com.dj.ckw.userservice.grpc.UserIdentityConfirmationResponse;
import com.dj.ckw.userservice.grpc.UserIdentityConfirmationServiceGrpc;
import com.dj.ckw.userservice.exception.UserNotFoundException;
import com.dj.ckw.userservice.model.User;
import com.dj.ckw.userservice.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GrpcUserService extends UserIdentityConfirmationServiceGrpc.UserIdentityConfirmationServiceImplBase {
  private static final Logger log = LoggerFactory.getLogger(GrpcUserService.class);
  private final UserRepository userRepository;

  public GrpcUserService(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
    log.info("GrpcUserService initialized");
  }

  @Override
  public void confirmUserIdentity(UserIdentityConfirmationRequest request,
      StreamObserver<UserIdentityConfirmationResponse> responseStreamObserver) {
    try {
      Optional<User> user = userRepository.findByEmail(request.getEmail());

      if (user.isEmpty()) {
        throw new UserNotFoundException(request.getEmail());
      }

      UserIdentityConfirmationResponse response = UserIdentityConfirmationResponse.newBuilder()
          .setIsConfirmed(user.get().getVerified())
          .build();

      responseStreamObserver.onNext(response);
      responseStreamObserver.onCompleted();
    } catch (UserNotFoundException e) {
      responseStreamObserver.onError(e);
      responseStreamObserver.onCompleted();
    }
  }
}
