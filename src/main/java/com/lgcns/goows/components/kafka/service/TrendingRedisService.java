package com.lgcns.goows.components.kafka.service;

import com.lgcns.goows.components.kafka.dto.TrendingFetchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TrendingRedisService {

    private final RedisTemplate<String, TrendingFetchDto> redisTemplate;

    public void saveTempData(String key, TrendingFetchDto data, long ttlSeconds) {
        redisTemplate.opsForValue().set("temp:" + key, data, ttlSeconds, TimeUnit.SECONDS);
    }

    public TrendingFetchDto getTempData(String key) {
        return (TrendingFetchDto) redisTemplate.opsForValue().get("temp:" + key);
    }

    public void deleteTempData(String key) {
        redisTemplate.delete("temp:" + key);
    }
}
