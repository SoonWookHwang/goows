package com.lgcns.goows.components.kafka;

import com.lgcns.goows.components.kafka.dto.BaseKafkaMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
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
        return new NewTopic("news-search-topic", 1, (short) 1);
    }
    @Bean
    public NewTopic top5keywordsTopic() {
        return new NewTopic("top5-keywords", 1, (short) 1);
    }

    @Bean
    public NewTopic trendingKeywordsTopic() {
        return new NewTopic("trending-keywords", 1, (short) 1);
    }


    // ✅ DTO 타입 고정 ConsumerFactory
//    @Bean
//    public ConsumerFactory<String, NewsSearchSendDataDto> newsSearchConsumerFactory() {
//        JsonDeserializer<NewsSearchSendDataDto> deserializer = new JsonDeserializer<>(NewsSearchSendDataDto.class);
//        deserializer.addTrustedPackages("*"); // 보안 이슈 없는 내부 프로젝트면 "*" 가능
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
//    }
//
//    @Bean
//    public ConsumerFactory<String, NewsSearchFetchDataDto> newsSearchConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//
//        // ✅ ErrorHandlingDeserializer 감싸기
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//
//        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
//        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
//
//        // ✅ 여기에만 타입 정보 명시
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.lgcns.goows.components.kafka.dto.NewsSearchFetchDataDto");
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//
//        // ❌ 이거 제거
//        // JsonDeserializer<NewsSearchSendDataDto> jsonDeserializer = new JsonDeserializer<>(NewsSearchSendDataDto.class);
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, NewsSearchFetchDataDto> newsSearchKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, NewsSearchFetchDataDto> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(newsSearchConsumerFactory());
//        return factory;
//    }


    @Bean
    public ConsumerFactory<String, BaseKafkaMessage> kafkaBaseMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        // ⭐ 부모 타입으로 설정
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.lgcns.goows.components.kafka.dto.BaseKafkaMessage");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BaseKafkaMessage> kafkaBaseMessageListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BaseKafkaMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaBaseMessageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());
        return factory;
    }
}