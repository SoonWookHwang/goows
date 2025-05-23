//package com.lgcns.goows.components.kafka;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lgcns.goows.components.kafka.dto.NewsSearchSendDataDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/kafka")
//@RequiredArgsConstructor
//public class KafkaTestController {
//
//    private final KafkaProducerService kafkaProducerService;
//
//    private final String NEWS_SEARCH_TOPIC = "news-search-topic";
//
//
//    @PostMapping("/send/search/query")
//    public ResponseEntity<?> sendNewsSearchKeyword(@RequestBody NewsSearchSendDataDto searchKeyword) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String message = objectMapper.writeValueAsString(searchKeyword);
//        kafkaProducerService.sendMessage(NEWS_SEARCH_TOPIC,message);
//        return ResponseEntity.ok(searchKeyword);
//    }
//}