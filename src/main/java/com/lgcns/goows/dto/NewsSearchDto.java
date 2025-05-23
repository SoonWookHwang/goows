package com.lgcns.goows.dto;

import lombok.Data;

@Data
public class NewsSearchDto {
    private String keyword;
    private Integer offset;
    private Integer limit;
}
