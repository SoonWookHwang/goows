package com.lgcns.goows.components.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic createDressesTopic() {
        return new NewTopic("dresses", 1, (short) 1);
    }

    @Bean
    public NewTopic newsSearchQueryTopic() {
        return new NewTopic("news_search_query", 1, (short) 1);
    }
}