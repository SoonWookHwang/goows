package com.lgcns.goows.components.kafka.repository;

import com.lgcns.goows.components.kafka.entity.MemberKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberKeywordRepository extends JpaRepository<MemberKeyword,Long> {
    List<MemberKeyword> findAllByMember_MemberId(Long memberId);
}
