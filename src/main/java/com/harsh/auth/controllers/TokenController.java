package com.harsh.auth.controllers;

import com.harsh.auth.dtos.requests.LoginRequestDto;
import com.harsh.auth.dtos.requests.RefreshTokenRequestDTO;
import com.harsh.auth.dtos.responses.JwtResponseDTO;
import com.harsh.auth.services.impl.RefreshTokenServiceImpl;
import com.harsh.auth.entities.RefreshToken;
import com.harsh.auth.services.impl.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class TokenController {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtServiceImpl jwtService;

    @Autowired
    public TokenController(AuthenticationManager authenticationManager, RefreshTokenServiceImpl refreshTokenService, JwtServiceImpl jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody LoginRequestDto authRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());

                return new ResponseEntity<>(JwtResponseDTO.builder()
                        .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                        .token(refreshToken.getToken())
                        .build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception in User Service: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(
            @RequestBody
            RefreshTokenRequestDTO refreshTokenRequestDTO
    ) {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());

                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..."));
    }
}
