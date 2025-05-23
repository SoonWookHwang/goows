package com.lgcns.goows.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.lgcns.goows.global.exception.CustomException;
import org.springframework.stereotype.Service;

import com.lgcns.goows.entity.Member;
import com.lgcns.goows.repository.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminMemberService {

    private final MemberRepository memberRepository;

    public AdminMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // Admin 멤버 정보 가져오기
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public void setMemberActiveStatus(Long memberId, boolean newIsActive) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException("ID가 " + memberId + "인 회원을 찾을 수 없습니다."));
        if (newIsActive) {
            member.activate();
        } else {
            member.deactivate();
        }
    }

}
