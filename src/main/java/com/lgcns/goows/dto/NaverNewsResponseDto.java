package com.lgcns.goows.dto;

import lombok.Data;

import java.util.List;

@Data
public class NaverNewsResponseDto {
    private String lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;
    private List<NewsResponseDto> items;
    private Long memberId;
    private String keyword;
}
