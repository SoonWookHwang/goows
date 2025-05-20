package com.lgcns.goows.entity;

import java.time.LocalDateTime;

import com.lgcns.goows.dto.NewsScrapRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsScrap extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsScrapId;

    private String title;
    private String originallink;
    private String link;
    private String description;
    private LocalDateTime pubdate;
    private boolean isScrap;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;


    // NewsScrapRequestDto와 Member를 이용하여 NewsScrap 엔티티를 생성하는 생성자
    public NewsScrap(NewsScrapRequestDto dto, Member member) {
        this.title = dto.getTitle();
        this.originallink = dto.getOriginallink();
        this.link = dto.getLink();
        this.description = dto.getDescription();
        this.pubdate = dto.getPubdate();
        this.member = member;
        this.isScrap = true;
    }
}
