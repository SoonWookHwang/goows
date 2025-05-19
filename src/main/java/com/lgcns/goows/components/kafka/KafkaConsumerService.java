package com.lgcns.goows.components.kafka;

import com.lgcns.goows.components.kafka.dto.BaseKafkaMessage;
import com.lgcns.goows.components.kafka.dto.NewsTop5Dto;
import com.lgcns.goows.components.kafka.dto.NewsSearchSendDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
//
//    @KafkaListener(topics = "dresses", groupId = "test-group")
//    public void listen(ConsumerRecord<String, NewsSearchFetchDataDto> record) {
//        System.out.println("Received message: " + record.value());
//    }

//    @KafkaListener(topics = "news_search_query", groupId = "news-group", containerFactory = "kafkaListenerContainerFactory")
//    public void listen(NewsSearchFetchDataDto dto) {
//        System.out.println("Received DTO: " + dto);
//    }

    @KafkaListener(
            topics = "news-search-topic",
            groupId = "news-group",
            containerFactory = "kafkaBaseMessageListenerContainerFactory"
    )
    public void listenNews(BaseKafkaMessage message) {
        if (message instanceof NewsSearchSendDataDto dto) {
            log.info("🔹 Received SendData: {}", dto);
        }
    }
//
//
//    @KafkaListener(
//            topics = "top5-keywords",
//            groupId = "news-group",
//            containerFactory = "kafkaBaseMessageListenerContainerFactory"
//    )
//    public void listenTop5Keyword(BaseKafkaMessage message) {
//        if (message instanceof NewsTop5Dto dto) {
//            log.info("🔹 Received SendData: {}", dto);
//        }
//    }

    @KafkaListener(
            topics = "top5-keywords",
            groupId = "news-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void listenTop5Keyword(String message) {
        log.info("🔹 Received SendData: {}", message);
    }

    @KafkaListener(
            topics = "trending-keywords",
            groupId = "news-group",
            containerFactory = "kafkaBaseMessageListenerContainerFactory"
    )
    public void listenTrendingKeyword(NewsSearchSendDataDto dto) {
        log.info("listenTop5Keyword call");
        System.out.println("🎯 수신된 메시지: " + dto);
    }
}