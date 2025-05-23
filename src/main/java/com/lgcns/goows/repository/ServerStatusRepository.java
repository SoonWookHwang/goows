package com.lgcns.goows.repository;

import com.lgcns.goows.entity.ServerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServerStatusRepository extends JpaRepository<ServerStatus, Long> {
    Optional<ServerStatus> findTopByOrderByStatusIdDesc();
}
