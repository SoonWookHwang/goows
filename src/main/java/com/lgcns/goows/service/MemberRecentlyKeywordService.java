package com.lgcns.goows.service;

import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.MemberRecentlyKeyword;
import com.lgcns.goows.global.security.UserDetailsImpl;
import com.lgcns.goows.repository.MemberRecentlyKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberRecentlyKeywordService {

    private final MemberRecentlyKeywordRepository memberRecentlyKeywordRepository;

    @Transactional
    public void addRecentlyKeyword(UserDetailsImpl userDetails,String keyword) {
        if(userDetails != null) {
            Member loginUser = userDetails.getMember();
            memberRecentlyKeywordRepository.save(MemberRecentlyKeyword.builder()
                    .member(loginUser)
                    .keyword(keyword).build());
        }
    }

    @Transactional(readOnly = true)
    public List<MemberRecentlyKeyword> getRecentlyKeywords(UserDetailsImpl userDetails) {
        if(userDetails!=null){
            Member loginUser = userDetails.getMember();
            return memberRecentlyKeywordRepository.findTop10DistinctByKeywordOrderByCreatedAtDesc(loginUser,PageRequest.of(0, 10));
        }
        return null;
    }

}
