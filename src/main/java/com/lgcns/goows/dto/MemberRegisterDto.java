package com.lgcns.goows.dto;

import lombok.Data;

@Data
public class MemberRegisterDto {
    private String username;
    private String password;
    private String nickname;
    private boolean isAdmin = false;
}
