package com.harsh.expense_tracker_backend.service;

import com.harsh.expense_tracker_backend.entities.UserInfo;
import com.harsh.expense_tracker_backend.model.UserInfoDto;
import com.harsh.expense_tracker_backend.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Could not found the user!!");
        }

        return new CustomUserDetails(user);
    }
    public UserInfo checkIfUserExists(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto) {
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getUsername()));

        if(checkIfUserExists(userInfoDto) != null) {
            return false;
        }

        String userId = UUID.randomUUID().toString();

        userRepository.save(new UserInfo(userId,
                userInfoDto.getUsername(),
                userInfoDto.getPassword(),
                new HashSet<>()));

        return true;
    }
}
