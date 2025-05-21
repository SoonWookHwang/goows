package com.lgcns.goows.components.kafka.controller;

import com.lgcns.goows.components.kafka.dto.TrendingFetchDto;
import com.lgcns.goows.components.kafka.service.MemberKeywordService;
import com.lgcns.goows.components.kafka.service.TrendingRedisService;
import com.lgcns.goows.global.common.SuccessResponse;
import com.lgcns.goows.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrendingController {
    private final TrendingRedisService trendingRedisService;
    private final MemberKeywordService memberKeywordService;

    @GetMapping("/trending/keywords")
    public ResponseEntity<?> getTrendingKeywords() {
        TrendingFetchDto data = trendingRedisService.getTempData("trendingKeywords");
        if (data == null) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(SuccessResponse.success(data)); // 200 OK
    }
    @GetMapping("/recommend/keywords")
    public ResponseEntity<?> getRecommendKeywords(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> keywords = memberKeywordService.getTop5Keyword(userDetails);
        return ResponseEntity.ok(SuccessResponse.success(keywords));
    }
}
