package com.harsh.auth.controllers;

import com.harsh.auth.dtos.responses.JwtResponseDTO;
import com.harsh.auth.services.impl.RefreshTokenServiceImpl;
import com.harsh.auth.entities.RefreshToken;
import com.harsh.auth.dtos.requests.UserInfoDto;
import com.harsh.auth.services.impl.JwtServiceImpl;
import com.harsh.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtServiceImpl jwtService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final UserService userService;

    @Autowired
    public AuthController(JwtServiceImpl jwtService, RefreshTokenServiceImpl refreshTokenService, UserService userService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @RequestBody
            UserInfoDto userInfoDto
    ) {
        try {
            String userId = userService.signupUser(userInfoDto);

            if(userId == null) {
                return new ResponseEntity<>("Already Exists!", HttpStatus.BAD_REQUEST);
            }

            RefreshToken refreshToken  = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());

            return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken).token(refreshToken.getToken()).userId(userId).build(), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Exception in User Service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            String userId = userService.getUserIdByUsername(authentication.getName());
            if(userId != null) {
                return new ResponseEntity<>(Map.of("userId", userId), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(Map.of("status", "unauthorized"), HttpStatus.UNAUTHORIZED);
    }
}
