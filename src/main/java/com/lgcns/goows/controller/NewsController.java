package com.lgcns.goows.controller;

import com.lgcns.goows.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.goows.dto.NewsScrapRequestDto;
import com.lgcns.goows.global.common.SuccessResponse;
import com.lgcns.goows.service.NewsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@Slf4j
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

  @GetMapping("/news")
    public ResponseEntity<?> getNewsKeyword(@RequestParam("param") String keyword,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(SuccessResponse.success(newsService.searchNews(userDetails,keyword, 10, "sim")));
    }

    @PostMapping("/news/scrap")
    public ResponseEntity<String> postNewsScrap(@RequestBody List<NewsScrapRequestDto> dtos) {
        log.info("뉴스 스크랩 요청 받음: {}, Member: {}", dtos);
        newsService.scrapNews(dtos);
        return new ResponseEntity<>("뉴스 스크랩 요청이 처리되었습니다.", HttpStatus.OK);
    }
}
