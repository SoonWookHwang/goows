package com.lgcns.goows.components.kafka.entity;

import com.lgcns.goows.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MemberKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberKeywordId;

    private String keyword;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    public static MemberKeyword toEntity(String keyword,Member member){
        return MemberKeyword.builder()
                .keyword(keyword)
                .member(member).build();
    }
}
