package com.lgcns.goows.components.kafka;

import com.lgcns.goows.components.kafka.dto.NewsSearchSendDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaProducerService kafkaProducerService;

    private final String NEWS_SEARCH_TOPIC = "news_search_topic";

//    @PostMapping("/send")
//    public String send(@RequestParam String message) {
//        kafkaProducerService.sendMessage("dresses", message);
//        return "Sent: " + message;
//    }

    @PostMapping("/send/search/query")
    public ResponseEntity<?> sendNewsSearchKeyword(@RequestBody NewsSearchSendDataDto searchKeyword){
        kafkaProducerService.sendMessage(NEWS_SEARCH_TOPIC, searchKeyword);
        return ResponseEntity.ok(searchKeyword);
    }
}