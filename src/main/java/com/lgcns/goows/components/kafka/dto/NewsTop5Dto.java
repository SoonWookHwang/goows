package com.lgcns.goows.components.kafka.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewsTop5Dto {
    private Long memberId;
    private List<String> top5;
}
