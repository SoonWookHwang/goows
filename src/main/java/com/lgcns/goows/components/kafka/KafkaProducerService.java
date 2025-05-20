package com.lgcns.goows.components.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final int MAX_RETRIES = 3;

    public void sendMessage(String topic, String message) {
        sendWithRetry(topic, message, 0);
    }

    private void sendWithRetry(String topic, String message, int retryCount) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.thenAccept(result -> {
            log.info("Kafka 메시지 전송 성공 | topic={}, offset={}, message={}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().offset(),
                    message);
        }).exceptionally(ex -> {
            log.error("Kafka 메시지 전송 실패 | topic={}, message={}, error={}",
                    topic, message, ex.getMessage(), ex);
            if (retryCount < MAX_RETRIES) {
                log.warn("🔁 Kafka 재시도 {}/{}...", retryCount + 1, MAX_RETRIES);
                sendWithRetry(topic, message, retryCount + 1);
            } else {
                log.error("🚫 Kafka 전송 재시도 초과 | message dropped: {}", message);
            }
            return null;
        });
    }
}