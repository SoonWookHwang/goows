package com.lgcns.goows.components.aspect;

import com.lgcns.goows.global.common.ErrorResponse;
import com.lgcns.goows.service.ServerStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ServerStatusAspect {

    private final ServerStatusService serverStatusService;

    @Around("execution(* com.lgcns.goows.controller.*.*(..))")
    public Object checkServerStatus(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!serverStatusService.getStatus()) {
            log.warn("🚫 서버 비활성화 상태 - 요청 차단: {}", joinPoint.getSignature());
            return ResponseEntity
                    .status(503)
                    .body(ErrorResponse.of(HttpStatus.SERVICE_UNAVAILABLE, "현재 서버 점검 중입니다."));
        }
        return joinPoint.proceed(); // 정상 요청 처리
    }
}