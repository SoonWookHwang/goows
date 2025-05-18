package com.lgcns.goows.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lgcns.goows.dto.NewsScrapRequestDto;
import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.NewsScrap;
import com.lgcns.goows.entity.Role;
import com.lgcns.goows.repository.NewsScapRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsScapRepository newsScapRepository;

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
