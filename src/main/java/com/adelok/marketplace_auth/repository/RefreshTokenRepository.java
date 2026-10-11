package com.adelok.marketplace_auth.repository;

import com.adelok.marketplace_auth.entity.RefreshToken;
import com.adelok.marketplace_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByUser(User user);
    void deleteByUser(User user);
}
