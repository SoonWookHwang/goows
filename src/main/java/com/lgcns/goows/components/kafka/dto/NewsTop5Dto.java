package com.lgcns.goows.components.kafka.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewsTop5Dto extends BaseKafkaMessage {
    private Long memberId;
    private String keyword;
    private List<NewsSearchSendDataDto.NewsItemDto> newsList;

    @Data
    public static class NewsItemDto {
        private String title;
        private String description;
        private String pubDate;
    }
}
