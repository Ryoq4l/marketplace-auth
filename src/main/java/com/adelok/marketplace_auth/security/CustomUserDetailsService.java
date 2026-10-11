package com.adelok.marketplace_auth.security;

import com.adelok.marketplace_auth.entity.User;
import com.adelok.marketplace_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {
    private final UserRepository userRepository;

    public UserDetails loadUserBySteamId(String steamId){
        User user = userRepository.findBySteamId(steamId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with steamId: " + steamId));
        return new CustomUserDetails(user);
    }
}
