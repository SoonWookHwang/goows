package com.lgcns.goows.service;

import com.lgcns.goows.dto.MemberModifyDto;
import com.lgcns.goows.dto.MemberRegisterDto;
import com.lgcns.goows.entity.Member;
import com.lgcns.goows.global.exception.CustomException;
import com.lgcns.goows.mappers.MemberMapper;
import com.lgcns.goows.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(MemberRegisterDto dto) {
        memberRepository.findByUsername(dto.getUsername())
                .ifPresent(user -> {
                    throw new CustomException("이미 존재하는 회원입니다.");
                });
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Member newMember = MemberMapper.toEntity(dto);
        memberRepository.save(newMember);
    }

    @Transactional
    public void modifyMemberInfo(String username, MemberModifyDto dto) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 회원 정보를 찾을 수 없습니다."));

        if (dto.getNickname() != null && !member.getNickname().equals(dto.getNickname())) {
            member.modifyNickname(dto.getNickname());
        } else if (dto.getNickname() != null && member.getNickname().equals(dto.getNickname())) {
            throw new IllegalArgumentException("기존 닉네임과 동일합니다.");
        }

        if (dto.getNewPassword() != null) {
            if (dto.getCurrentPassword() == null) {
                throw new IllegalArgumentException("현재 비밀번호를 입력해주세요.");
            }
            if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }
            member.modifyPassword(passwordEncoder.encode(dto.getNewPassword()));
        }
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new CustomException("회원정보가 존재하지 않습니다"));
    }


}
