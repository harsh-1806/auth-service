package com.harsh.auth.controllers;

import com.harsh.auth.responses.JwtResponseDTO;
import com.harsh.auth.services.RefreshTokenService;
import com.harsh.auth.entities.RefreshToken;
import com.harsh.auth.model.UserInfoDto;
import com.harsh.auth.services.JwtService;
import com.harsh.auth.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailServiceImpl userDetailService;

    @Autowired
    public AuthController(JwtService jwtService, RefreshTokenService refreshTokenService, UserDetailServiceImpl userDetailService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userDetailService = userDetailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @RequestBody
            UserInfoDto userInfoDto
    ) {
        try {
            String userId = userDetailService.signupUser(userInfoDto);
            if(userId == null) {
                return new ResponseEntity<>("Already Exists!", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken  = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());

            return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken).token(refreshToken.getToken()).userId(userId).build(), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Exception in User Service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            String userId = userDetailService.getUserIdByUsername(authentication.getName());
            if(userId != null) {
                return new ResponseEntity<>(userId, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
}
