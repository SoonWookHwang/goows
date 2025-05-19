package com.lgcns.goows.dto;

import lombok.Data;

@Data
public class MemberModifyDto {

    private String currentPassword;
    private String newPassword;
    private String nickname;
}
