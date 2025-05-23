package com.lgcns.goows.repository;

import com.lgcns.goows.entity.Member;
import com.lgcns.goows.entity.MemberRecentlyKeyword;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRecentlyKeywordRepository extends JpaRepository<MemberRecentlyKeyword, Long> {

    @Query("""
                SELECT mrk FROM MemberRecentlyKeyword mrk
                WHERE mrk.createdAt IN (
                    SELECT MAX(mrk2.createdAt)
                    FROM MemberRecentlyKeyword mrk2
                    WHERE mrk2.member = :member
                    GROUP BY mrk2.keyword
                )
                ORDER BY mrk.createdAt DESC
            """)
    List<MemberRecentlyKeyword> findTop10DistinctByKeywordOrderByCreatedAtDesc(@Param("member") Member member, Pageable pageable);
}
