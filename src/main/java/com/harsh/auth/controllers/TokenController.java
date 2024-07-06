package com.harsh.auth.controllers;

import com.harsh.auth.requests.AuthRequestDTO;
import com.harsh.auth.requests.RefreshTokenRequestDTO;
import com.harsh.auth.responses.JwtResponseDTO;
import com.harsh.auth.services.RefreshTokenService;
import com.harsh.auth.entities.RefreshToken;
import com.harsh.auth.services.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("auth/v1")
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TokenController(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

//    @PostMapping("/login")
//    public ResponseEntity AuthenticateAndGetToken(
//            @RequestBody
//            AuthRequestDTO authRequestDTO
//    )
//    {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
//        if(authentication.isAuthenticated()) {
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
//
//            return new ResponseEntity<>(JwtResponseDTO.builder()
//                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
//                    .token(refreshToken.getToken())
//                    .build(), HttpStatus.OK);
//        }
//        else {
//            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            logger.info("Attempting to authenticate user: {}", authRequestDTO.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                logger.info("User authenticated: {}", authRequestDTO.getUsername());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());

                return new ResponseEntity<>(JwtResponseDTO.builder()
                        .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                        .token(refreshToken.getToken())
                        .build(), HttpStatus.OK);
            } else {
                logger.warn("Authentication failed for user: {}", authRequestDTO.getUsername());
                return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e) {
            logger.error("Invalid username or password for user: {}", authRequestDTO.getUsername());
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Exception in User Service: {}", e.getMessage());
            e.printStackTrace();  // Log the exception
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
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());

                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..."));
    }
}
