package com.lgcns.goows.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final long refreshTokenTtl = 60 * 60 * 24 * 7L; // 7일 (초 단위)
    private final String prefix = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(prefix + username, refreshToken, refreshTokenTtl, TimeUnit.SECONDS);
    }

    public boolean isValidRefreshToken(String username, String refreshToken) {
        String savedToken = redisTemplate.opsForValue().get(prefix + username);
        return refreshToken.equals(savedToken);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(prefix + username);
    }
    public void blacklistAccessToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isAccessTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
}