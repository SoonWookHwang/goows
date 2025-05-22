package com.lgcns.goows.repository;

import com.lgcns.goows.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    List<Member> findAll(); // Admin 모든 회원 조회

}
