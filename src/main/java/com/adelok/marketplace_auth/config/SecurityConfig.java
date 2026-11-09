package com.adelok.marketplace_auth.config;

import com.adelok.marketplace_auth.security.JwtAuthFilter;
import com.adelok.marketplace_auth.security.MultiSessionOAuth2AuthorizationRequestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new MultiSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(ep -> ep
                                .authorizationRequestRepository(authorizationRequestRepository)
                        )
                        .defaultSuccessUrl("/oauth-success", true)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/error",
                                "/oauth2/**", "/login/oauth2/**",
                                "/oauth-success",
                                "/api/v1/auth/oauth-success"
                        ).permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}