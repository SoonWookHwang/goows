package com.lgcns.goows.components.kafka.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TrendingFetchDto {
    private LocalDateTime timestamp;
    private List<KeywordDto> top_keywords;
    @Data
    public static class KeywordDto {
        private String keyword;
        private int count;
    }
}
