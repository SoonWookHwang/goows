package com.lgcns.goows.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.goows.dto.NewsScrapRequestDto;
import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.NewsScrap;
import com.lgcns.goows.entity.Role;
import com.lgcns.goows.repository.MemberRepository;
import com.lgcns.goows.repository.NewsScapRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final MemberRepository memberRepository;
    private final NewsScapRepository newsScapRepository;
    
    @Value("${naver.api.client-id}")
    String clientId;
    // String clientId = "cP2S3qhKex1szo5gWypN";

    @Value("${naver.api.client-secret}")
    String clientSecret;
    // String clientSecret = "EOAQDpIfwu";

    @Value("${naver.api.news-url}")
    String newsSearchUrl;
    
    // String newsSearchUrl = "https://openapi.naver.com/v1/search/news.json";

    public Map<String, Object> searchNews(String query, Integer display, String sort) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        String url = String.format("%s?query=%s&display=%d&sort=%s", newsSearchUrl, query, display, sort);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        String responesBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(responesBody, Map.class);
        } catch(IOException e){
            //제이슨 파싱 실패 처리
            e.printStackTrace();
            return null; //또는 예외를 던지는 방식
        }
    }


    @Transactional
    public void scrapNews(List<NewsScrapRequestDto> dtos) {
        Member member = Member.builder().memberId(1l)
                                        .nickname("")
                                        .password("null")
                                        .role(Role.ROLE_USER)
                                        .username("null").build();

        for (NewsScrapRequestDto dto : dtos) {
            log.info("Scrap 요청 DTO: {}, Member ID: {}", dto, member.getMemberId());
            // title, originallink, member로 이미 저장된 뉴스가 있는지 확인
            Optional<NewsScrap> existingNews = newsScapRepository.findByTitleAndOriginallinkAndMember(dto.getTitle(), dto.getOriginallink(), member);

            if (existingNews.isPresent()) {
                log.info("이미 저장된 뉴스입니다. title: {}, Member ID: {}", dto.getTitle(), member.getMemberId());
                // 이미 저장된 뉴스가 있다면 아무것도 하지 않거나, 필요한 경우 정보를 반환할 수 있습니다.
            } else {
                // 새로운 NewsScrap 엔티티를 생성하여 저장
                NewsScrap newsScrap = new NewsScrap(dto, member);
                NewsScrap savedNews = newsScapRepository.save(newsScrap);
                log.info("새로운 뉴스를 저장했습니다. ID: {}, Member ID: {}", savedNews.getNewsScrapId(), member.getMemberId());
            }
        }
    }
}
