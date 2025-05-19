//package com.lgcns.goows.components.kafka;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.common.serialization.Deserializer;
//
//import java.util.Map;
//
//public class GenericKafkaJsonDeserializer<T> implements Deserializer<T> {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private Class<T> targetType;
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void configure(Map<String, ?> configs, boolean isKey) {
//        String className = (String) configs.get("value.deserializer.target.type");
//        try {
//            targetType = (Class<T>) Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("Deserializer: cannot find class " + className, e);
//        }
//    }
//
//    @Override
//    public T deserialize(String topic, byte[] data) {
//        if (data == null || targetType == null) return null;
//        try {
//            return objectMapper.readValue(data, targetType);
//        } catch (Exception e) {
//            throw new RuntimeException("Deserializer: error reading JSON", e);
//        }
//    }
//
//    @Override
//    public void close() {
//    }
//}