package com.lgcns.goows.components.kafka.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class NewsSearchSendDataDto {
    private Long memberId;
    private String keyword;
    private List<NewsItemDto> newsList;

    @Data
    public static class NewsItemDto {
        private String title;
        private String description;
        private String pubDate;
    }
    @Builder
    public NewsSearchSendDataDto(Map<String, Object> map) {
        memberId = (Long) map.get("memberId");
        keyword = (String) map.get("keyword");
        List<Map<String, Object>> items = (List<Map<String, Object>>) map.get("items");
        newsList = new ArrayList<>();
        if (items != null) {
            for (Map<String, Object> item : items) {
                NewsItemDto dto = new NewsItemDto();
                dto.setTitle((String) item.get("title"));
                dto.setDescription((String) item.get("description"));
                dto.setPubDate((String) item.get("pubDate"));
                this.newsList.add(dto);
            }
        }
    }
}

