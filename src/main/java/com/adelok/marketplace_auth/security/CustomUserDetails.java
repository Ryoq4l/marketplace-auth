package com.adelok.marketplace_auth.security;

import com.adelok.marketplace_auth.entity.User;
import com.adelok.marketplace_auth.entity.enumerated.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;
//TODO: Move the role transformation with the prefix to a separate location
    public List<GrantedAuthority> getAuthorities(){
        String authority = "ROLE_" + user.getRole().name();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
        return List.of(grantedAuthority);
    }

    public User getUser() {
        return user;
    }

    public String getPassword(){
        return user.getPasswordHash();
    }
    public String getUsername(){
        return user.getEmail();
    }
    public boolean isAccountNonExpired(){
        return true;
    }
    public boolean isAccountNonLocked(){
        return user.getStatus() == UserStatus.ACTIVE;
    }
    public boolean isCredentialsNonExpired(){
        return true;
    }
    public boolean isEnabled(){
        return true;
    }
}
