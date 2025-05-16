package com.lgcns.goows.mappers;

import com.lgcns.goows.dto.MemberRegisterDto;
import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.Role;

public class MemberMapper {

    public static Member toEntity(MemberRegisterDto dto) {
        return Member.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .role(dto.isAdmin() ? Role.ROLE_ADMIN : Role.ROLE_USER)
                .build();
    }
}
