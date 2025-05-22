package com.lgcns.goows.components.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lgcns.goows.components.kafka.dto.NewsTop5Dto;
import com.lgcns.goows.components.kafka.dto.TrendingFetchDto;
import com.lgcns.goows.components.kafka.service.MemberKeywordService;
import com.lgcns.goows.components.kafka.service.TrendingRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final MemberKeywordService memberKeywordService;
    private final TrendingRedisService kafkaService;
    @KafkaListener(
            topics = "top5-keywords",
            groupId = "news-group2",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void listenTop5Keyword(String message) throws JsonProcessingException {
        log.info("🔹 Received top5: {}", message);
        ObjectMapper mapper = new ObjectMapper();
        NewsTop5Dto dto = mapper.readValue(message, NewsTop5Dto.class);
        memberKeywordService.saveTop5Keyword(dto);
        log.info("top5 listen value : {}", dto.toString());
    }

    @KafkaListener(
            topics = "trending-keywords",
            groupId = "news-group2",
            containerFactory = "kafkaBaseMessageListenerContainerFactory"
    )
    public void listenTrendingKeyword(String message) throws JsonProcessingException {
//        log.info(message);
        TrendingFetchDto dto = parsingTrendingFetchDto(message);
//        log.info("🎯 수신된 메시지: " + dto.toString());
        kafkaService.saveTempData("trendingKeywords",dto,5000);
    }

    public TrendingFetchDto parsingTrendingFetchDto(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // ✅ ISO 8601 형식 "2025-05-21T05:07:10Z" → LocalDateTime 변환 지원
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE); // Z를 LocalTime으로 그대로 해석
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        TrendingFetchDto dto = mapper.readValue(message,TrendingFetchDto.class);
        LocalDateTime kstTime = dto.getTimestamp()
                .atOffset(ZoneOffset.UTC)
                .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
        dto.setTimestamp(kstTime);
        return dto;
    }
}