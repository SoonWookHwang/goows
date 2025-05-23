package com.lgcns.goows.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.goows.components.kafka.KafkaProducerService;
import com.lgcns.goows.components.kafka.dto.NewsSearchSendDataDto;
import com.lgcns.goows.components.naver.HtmlImageParser;
import com.lgcns.goows.components.naver.NaverApiComponent;
import com.lgcns.goows.dto.*;
import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.NewsScrap;
import com.lgcns.goows.global.exception.CustomException;
import com.lgcns.goows.global.security.UserDetailsImpl;
import com.lgcns.goows.repository.MemberRecentlyKeywordRepository;
import com.lgcns.goows.repository.MemberRepository;
import com.lgcns.goows.repository.NewsScapRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final MemberRepository memberRepository;
    private final NewsScapRepository newsScapRepository;
    private final KafkaProducerService kafkaProducerService;
    private final NaverApiComponent naverApiComponent;
    private final MemberRecentlyKeywordService memberRecentlyKeywordService;
    private final String NEWS_SEARCH_TOPIC = "news-search-topic";
    private final MemberRecentlyKeywordRepository memberRecentlyKeywordRepository;

    public NaverNewsResponseDto searchNews(UserDetailsImpl userDetails, NewsSearchDto dto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = naverApiComponent.getNewsDataFromNaver(dto);
            // 2. JSON → DTO 변환
            NaverNewsResponseDto result = objectMapper.readValue(responseBody, NaverNewsResponseDto.class);

            // 3. 추가 정보 세팅
            result.setMemberId(userDetails != null ? userDetails.getMember().getMemberId() : 0L);
            result.setKeyword(dto.getKeyword());

            List<NewsResponseDto> list = result.getItems();
            if (userDetails != null) {
                Member loginUser = userDetails.getMember();
                list.stream().forEach(nrd -> {
                    boolean isScraped = newsScapRepository
                            .findByLinkAndOriginallinkAndMember(nrd.getLink(), nrd.getOriginallink(), loginUser)
                            .isPresent();
                    nrd.setScraped(isScraped);
                    nrd.setImageUrl(HtmlImageParser.extractHtmlBodyAndImgSrc(nrd.getLink()));
                });
                memberRecentlyKeywordService.addRecentlyKeyword(userDetails, dto.getKeyword());
            } else {
                list.stream().forEach(nrd -> {
                    nrd.setScraped(false);
                    nrd.setImageUrl(HtmlImageParser.extractHtmlBodyAndImgSrc(nrd.getLink()));
                });
            }
            //// 검색 시 마다 검색 된 뉴스 리스트를 카프카 프로듀싱 ////
            Map<String, Object> map = objectMapper.convertValue(result, new TypeReference<>() {});
            String message = objectMapper.writeValueAsString(
                NewsSearchSendDataDto.builder().map(map).build()
            );
            kafkaProducerService.sendMessage(NEWS_SEARCH_TOPIC, message);
            ///////////////////////////////////////////////////
            return result;
        } catch (JsonProcessingException e) {
            throw new CustomException(e.getMessage());
        }
    }


    @Transactional
    public void toggleScrapNews(UserDetailsImpl userDetails, NewsScrapRequestDto dto) {
        Member loginUser = userDetails.getMember();
        // link, originallink, member로 이미 저장된 뉴스가 있는지 확인
        Optional<NewsScrap> existingNews = newsScapRepository.findByLinkAndOriginallinkAndMember(dto.getLink(), dto.getOriginallink(), loginUser);
        if (existingNews.isPresent()) {
            newsScapRepository.delete(existingNews.get());
            log.info("기존 스크랩된 뉴스를 삭제하였습니다");
        } else {
            NewsScrap newsScrap = new NewsScrap(dto, loginUser);
            NewsScrap savedNews = newsScapRepository.save(newsScrap);
            log.info("새로운 뉴스를 저장했습니다. ID: {}, Member ID: {}", savedNews.getNewsScrapId(), loginUser.getMemberId());
        }
    }

    public Page<NewsScrap> getNewsScrap(UserDetailsImpl userDetails, NewsScrapSearchDto dto) {
        int offset = dto.getOffset() != null ? dto.getOffset() : 0;
        int limit = dto.getLimit() != null ? dto.getLimit() : 10;
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        Page<NewsScrap> scrapPages = newsScapRepository.searchNewsScrapsByKeyword(userDetails.getMember(), dto.getKeyword(), pageable);
        return scrapPages;
    }
}
