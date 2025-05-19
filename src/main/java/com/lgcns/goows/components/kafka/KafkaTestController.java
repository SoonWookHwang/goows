package com.lgcns.goows.components.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaProducerService kafkaProducerService;

    private final String NEWS_SEARCH_TOPIC = "news_search_topic";

    @PostMapping("/send")
    public String send(@RequestParam String message) {
        kafkaProducerService.sendMessage("dresses", message);
        return "Sent: " + message;
    }

    @PostMapping("/send/search/query")
    public String sendNewsSearchKeyword(@RequestParam String searchKeyword){
        kafkaProducerService.sendMessage(NEWS_SEARCH_TOPIC, searchKeyword);
        return "Sent news Query: " + searchKeyword;
    }
}