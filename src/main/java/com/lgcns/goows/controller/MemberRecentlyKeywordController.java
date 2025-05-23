package com.lgcns.goows.controller;

import com.lgcns.goows.global.security.UserDetailsImpl;
import com.lgcns.goows.service.MemberRecentlyKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberRecentlyKeywordController {

    private final MemberRecentlyKeywordService recentlyKeywordService;

    @GetMapping("/recently/keywords")
    public ResponseEntity<?> getRecentlyKeywords(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(recentlyKeywordService.getRecentlyKeywords(userDetails));
    }
}
