package com.harsh.auth.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    Date extractExpiration(String token);
    Boolean isTokenExpired(String token);
    String generateToken(String username);
    Boolean validateToken(String token, UserDetails userDetails);
    <T> T extractClaim(String token, Function<Claims, T> claimResolver);
    Claims extractAllClaims(String token);
}
