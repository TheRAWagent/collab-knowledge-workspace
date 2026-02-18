package com.dj.ckw.authservice.service;

import com.dj.ckw.authservice.model.User;
import com.dj.ckw.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;

  protected UserService() {
    this.userRepository = null;
  }

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
