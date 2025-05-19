package com.lgcns.goows.components.kafka;

import com.lgcns.goows.components.kafka.dto.NewsSearchFetchDataDto;
import com.lgcns.goows.components.kafka.dto.NewsSearchSendDataDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
            topics = "news_search_topic",
            groupId = "news-group",
            containerFactory = "newsSearchKafkaListenerContainerFactory"
    )
    public void listenNews(NewsSearchSendDataDto dto) {
        log.info("listen call");
        System.out.println("🎯 수신된 메시지: " + dto);
    }
}