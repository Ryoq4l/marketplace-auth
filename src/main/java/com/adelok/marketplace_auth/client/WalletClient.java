package com.adelok.marketplace_auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "marketplace-wallet")
public interface WalletClient {
    @PostMapping("/api/v1/wallets")
    void createWallet(@RequestBody CreateWalletRequest request);

    record CreateWalletRequest(UUID userId) {
    }
}
