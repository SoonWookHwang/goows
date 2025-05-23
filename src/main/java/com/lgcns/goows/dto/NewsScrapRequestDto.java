package com.lgcns.goows.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NewsScrapRequestDto {
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String imageUrl;
    private LocalDateTime pubdate;
}
