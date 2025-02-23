package com.harsh.auth.services.impl;

import com.harsh.auth.entities.UserRole;
import com.harsh.auth.eventProducer.UserInfoEvent;
import com.harsh.auth.eventProducer.UserInfoProducer;
import com.harsh.auth.respositories.UserRepository;
import com.harsh.auth.entities.User;
import com.harsh.auth.dtos.requests.UserInfoDto;
import com.harsh.auth.respositories.UserRoleRepository;
import com.harsh.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoProducer userInfoProducer;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserInfoProducer userInfoProducer, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoProducer = userInfoProducer;
        this.userRoleRepository = userRoleRepository;
    }

    public String getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return user.getUserId();
    }

    public User checkIfUserExists(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public String signupUser(UserInfoDto userInfoDto) {
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        if(checkIfUserExists(userInfoDto) != null){
            return null;
        }

        String userId = UUID.randomUUID().toString();
        userInfoDto.setUserId(userId);

        UserRole userRole = userRoleRepository.findByName("USER")
                .orElseGet(() -> {
                    UserRole newRole = new UserRole();
                    newRole.setName("USER");
                    return userRoleRepository.save(newRole);
                });

        userRoleRepository.save(userRole);

        User user = new User
                (
                        userId,
                        userInfoDto.getUsername(),
                        userInfoDto.getPassword(),
                        Set.of(userRole)
                );

        userRepository.save(user);

        //pushEventToMessagingService

        userInfoProducer.sendEventToKafka(userInfoToPublish(userInfoDto));

        return userId;
    }

    private UserInfoEvent userInfoToPublish(UserInfoDto userInfoDto) {
        return UserInfoEvent.builder()
                .userId(userInfoDto.getUserId())
                .firstName(userInfoDto.getFirstName())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber())
                .build();
    }

}
