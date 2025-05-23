package com.lgcns.goows.controller;

import com.lgcns.goows.dto.NewsScrapRequestDto;
import com.lgcns.goows.dto.NewsScrapSearchDto;
import com.lgcns.goows.dto.NewsSearchDto;
import com.lgcns.goows.global.common.SuccessResponse;
import com.lgcns.goows.global.security.UserDetailsImpl;
import com.lgcns.goows.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

  @GetMapping("/news")
    public ResponseEntity<?> getNewsKeyword(@ModelAttribute NewsSearchDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(SuccessResponse.success(newsService.searchNews(userDetails,dto)));
    }

    @PostMapping("/news/scrap")
    public ResponseEntity<?> toggleNewsScrap(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody NewsScrapRequestDto dto) {
        newsService.toggleScrapNews(userDetails,dto);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @GetMapping("/news/scrap")
    public ResponseEntity<?> getNewsScrap(@AuthenticationPrincipal UserDetailsImpl userDetails,@ModelAttribute NewsScrapSearchDto dto) {
        return ResponseEntity.ok(newsService.getNewsScrap(userDetails, dto));
    }
}
