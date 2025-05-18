package com.lgcns.goows.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.NewsScrap;
import java.util.List;
import java.util.Optional;


public interface NewsScapRepository extends JpaRepository<NewsScrap, Long> {

     Optional<NewsScrap> findByTitleAndOriginallinkAndMember(String title, String originallink, Member member);
}
    

