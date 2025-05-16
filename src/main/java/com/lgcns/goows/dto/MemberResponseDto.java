package com.lgcns.goows.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {

    private Long memberId;
    private String username;
    private String password;
    private String nickname;
}
