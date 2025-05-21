package com.lgcns.goows.global.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lgcns.goows.components.kafka.dto.TrendingFetchDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, TrendingFetchDto> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, TrendingFetchDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // ✅ Jackson2JsonRedisSerializer 설정 (최신 방식)
        Jackson2JsonRedisSerializer<TrendingFetchDto> serializer =
                new Jackson2JsonRedisSerializer<>(TrendingFetchDto.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 날짜 파싱
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // ✅ 최신 방식으로 ObjectMapper 주입
        serializer.setObjectMapper(objectMapper); // 이건 여전히 안전함 (직접 생성 시)

        // key/value 직렬화 방식 지정
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}