package com.lgcns.goows.admin;

import lombok.Data;

@Data
public class AdminMemberCountDto {

    private Integer totalMemberCount;
    private Integer activeMemberCount;
    private Integer inactiveMemberCount;
}
