package com.lgcns.goows.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MemberRecentlyKeyword extends TimeStamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recentlyKeywordId;

    private String keyword;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
