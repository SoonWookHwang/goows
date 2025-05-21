package com.lgcns.goows.service;

import com.lgcns.goows.entity.ServerStatus;
import com.lgcns.goows.repository.ServerStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServerStatusService {

    private final ServerStatusRepository serverStatusRepository;

    public boolean getStatus() {
        return serverStatusRepository.findTopByOrderByStatusIdDesc()
                .map(ServerStatus::isStatus)
                .orElse(true); // 기본값은 true
    }

    @Transactional
    public void toggleStatus() {
        boolean current = getStatus();
        ServerStatus updated = ServerStatus.builder()
                .status(!current)
                .build();
        serverStatusRepository.save(updated);
    }
}