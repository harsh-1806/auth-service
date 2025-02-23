package com.harsh.auth.services.impl;

import com.harsh.auth.entities.User;
import com.harsh.auth.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails extends User implements UserDetails {
    private final String username;
    private final String password;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password= user.getPassword();

        Set<GrantedAuthority> auths = new HashSet<>();

        for(UserRole role : user.getRoles()) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
