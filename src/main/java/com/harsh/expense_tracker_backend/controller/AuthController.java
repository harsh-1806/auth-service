package com.harsh.expense_tracker_backend.controller;

import com.harsh.expense_tracker_backend.entities.RefreshToken;
import com.harsh.expense_tracker_backend.model.UserInfoDto;
import com.harsh.expense_tracker_backend.response.JwtResponseDTO;
import com.harsh.expense_tracker_backend.service.JwtService;
import com.harsh.expense_tracker_backend.service.RefreshTokenService;
import com.harsh.expense_tracker_backend.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @PostMapping("auth/v1/signup")
    public ResponseEntity signUp(
            @RequestBody
            UserInfoDto userInfoDto
    ) {
        try {
            Boolean isSignedUp = userDetailService.signupUser(userInfoDto);
            if(Boolean.FALSE.equals(isSignedUp)) {
                return new ResponseEntity<>("Already Exists!", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken  = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());

            return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken).token(refreshToken.getToken()).build(), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Exception in User Service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
