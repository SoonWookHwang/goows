package com.lgcns.goows.service;

import com.lgcns.goows.dto.MemberRegisterDto;
import com.lgcns.goows.entity.Member;
import com.lgcns.goows.global.exception.CustomException;
import com.lgcns.goows.mappers.MemberMapper;
import com.lgcns.goows.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
                .ifPresent(user -> { throw new CustomException("이미 존재하는 회원입니다."); });
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Member newMember = MemberMapper.toEntity(dto);
        memberRepository.save(newMember);
    }
}
