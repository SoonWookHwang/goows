package com.lgcns.goows.dto;

import lombok.Data;

@Data
public class NewsResponseDto {
        private String title;
        private String originallink;
        private String link;
        private String description;
        private String pubDate;
        private String imageUrl="";
        private boolean isScraped;
}
