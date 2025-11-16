package com.dj.ckw.userservice.service;

import com.dj.ckw.userservice.dto.UserRequestDto;
import com.dj.ckw.userservice.dto.UserResponseDto;
import com.dj.ckw.userservice.exception.UserNotFoundException;
import com.dj.ckw.userservice.model.RequestInfo;
import com.dj.ckw.userservice.model.User;
import com.dj.ckw.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RequestInfo requestInfo;

    public UserService(UserRepository userRepository, RequestInfo requestInfo) {
        this.userRepository = userRepository;
        this.requestInfo = requestInfo;
    }

    public UserResponseDto getUser() {
        Optional<User> optionalUser = userRepository.findByEmail(requestInfo.getEmail());

        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException(requestInfo.getEmail());
        }

        User user = optionalUser.get();

        return new UserResponseDto(user.getName(), user.getEmail(), user.getAvatarUrl());
    }

    public void createUser(@Valid UserRequestDto userRequestDto) {
        User user = new User(requestInfo.getEmail(), userRequestDto.getName());

        userRepository.save(user);
    }

    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        Optional<User> optionalUser = userRepository.findByEmail(requestInfo.getEmail());

        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException(requestInfo.getEmail());
        }

        User user = optionalUser.get();
        if(userRequestDto.getName()!=null) {
            user.setName(userRequestDto.getName());
        }

//        More update fields to be added later

        userRepository.save(user);

        return  new UserResponseDto(user.getName(), user.getEmail(), user.getAvatarUrl());
    }
}
