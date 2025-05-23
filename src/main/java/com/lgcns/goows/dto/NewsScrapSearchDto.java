package com.lgcns.goows.dto;

import lombok.Data;

@Data
public class NewsScrapSearchDto {
    private String keyword;
    private Integer offset;
    private Integer limit;
}
