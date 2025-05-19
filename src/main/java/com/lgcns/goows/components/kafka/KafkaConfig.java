package com.lgcns.goows.components.kafka;

import com.lgcns.goows.components.kafka.dto.NewsSearchFetchDataDto;
import com.lgcns.goows.components.kafka.dto.NewsSearchSendDataDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic createDressesTopic() {
        return new NewTopic("dresses", 1, (short) 1);
    }

    @Bean
    public NewTopic newsSearchQueryTopic() {
        return new NewTopic("news_search_topic", 1, (short) 1);
    }

    // ✅ DTO 타입 고정 ConsumerFactory
    @Bean
    public ConsumerFactory<String, NewsSearchSendDataDto> newsSearchConsumerFactory() {
        JsonDeserializer<NewsSearchSendDataDto> deserializer = new JsonDeserializer<>(NewsSearchSendDataDto.class);
        deserializer.addTrustedPackages("*"); // 보안 이슈 없는 내부 프로젝트면 "*" 가능

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NewsSearchSendDataDto> newsSearchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NewsSearchSendDataDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(newsSearchConsumerFactory());
        return factory;
    }
}