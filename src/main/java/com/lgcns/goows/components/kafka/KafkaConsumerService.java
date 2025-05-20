package com.lgcns.goows.components.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.goows.components.kafka.dto.NewsTop5Dto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
    @KafkaListener(
            topics = "top5-keywords",
            groupId = "news-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void listenTop5Keyword(String message) throws JsonProcessingException {
        log.info("🔹 Received top5: {}", message);
        ObjectMapper mapper = new ObjectMapper();
        NewsTop5Dto dto = mapper.readValue(message, NewsTop5Dto.class);
        log.info("top5 listen value : {}", dto.toString());
    }

    @KafkaListener(
            topics = "trending-keywords",
            groupId = "news-group",
            containerFactory = "kafkaBaseMessageListenerContainerFactory"
    )
    public void listenTrendingKeyword(String message) {
        log.info("listenTop5Keyword call");
        log.info("🎯 수신된 메시지: " + message);
    }
}