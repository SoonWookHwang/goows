package com.lgcns.goows.components.kafka.service;

import com.lgcns.goows.components.kafka.dto.NewsTop5Dto;
import com.lgcns.goows.components.kafka.entity.MemberKeyword;
import com.lgcns.goows.components.kafka.repository.MemberKeywordRepository;
import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.Role;
import com.lgcns.goows.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberKeywordService {
    private final MemberKeywordRepository memberKeywordRepository;

    @Transactional
    public void saveTop5Keyword(NewsTop5Dto dto){
        if(dto.getMemberId()!=0) {
            List<MemberKeyword> top5keyword = memberKeywordRepository.findAllByMember_MemberId(dto.getMemberId());
            Member tempMember = Member.builder()
                    .memberId(dto.getMemberId())
                    .nickname("")
                    .role(Role.ROLE_USER).username("").password("").build();
            List<MemberKeyword> saveList = new ArrayList<>();
            if (!top5keyword.isEmpty()) {
                log.info("기존 저장된 데이터 있음");
                memberKeywordRepository.deleteAll(top5keyword);
            }
            saveList = dto.getTop5().stream().map(keyword -> MemberKeyword.toEntity(keyword, tempMember)).toList();
            List<MemberKeyword> newEntity = memberKeywordRepository.saveAll(saveList);
            log.info("새로 저장된 top5: {}", newEntity.toString());
            log.info("top5 keyword 저장완료");
        }
    }

    public List<String> getTop5Keyword(UserDetailsImpl userDetails){
        try {
            Member loginMember = userDetails.getMember();
            List<MemberKeyword> list = memberKeywordRepository.findAllByMember_MemberId(loginMember.getMemberId());
            List<String> keywords = new ArrayList<>();
            for (MemberKeyword memberKeyword : list) {
                keywords.add(memberKeyword.getKeyword());
            }
            return keywords;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }
}
