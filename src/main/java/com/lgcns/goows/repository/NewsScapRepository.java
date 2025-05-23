package com.lgcns.goows.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.NewsScrap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface NewsScapRepository extends JpaRepository<NewsScrap, Long> {
    Optional<NewsScrap> findByLinkAndOriginallinkAndMember(String title, String originallink, Member member);

    @Query("""
                SELECT n FROM NewsScrap n
                WHERE n.member = :member
                AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                     OR LOWER(n.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                ORDER BY n.createdAt DESC
            """)
    Page<NewsScrap> searchNewsScrapsByKeyword(@Param("member") Member member,
                                    @Param("keyword") String keyword,
                                    Pageable pageable);
}
    

