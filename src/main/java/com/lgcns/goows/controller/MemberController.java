package com.lgcns.goows.controller;

import com.lgcns.goows.dto.MemberLoginDto;
import com.lgcns.goows.dto.MemberRegisterDto;
import com.lgcns.goows.dto.TokenRequestDto;
import com.lgcns.goows.global.common.SuccessResponse;
import com.lgcns.goows.global.common.TokenResponse;
import com.lgcns.goows.global.security.JwtTokenProvider;
import com.lgcns.goows.service.MemberService;
import com.lgcns.goows.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> memberRegister(@RequestBody MemberRegisterDto dto) {
        memberService.register(dto);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.generateAccessToken(dto.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(dto.getUsername());

        refreshTokenService.saveRefreshToken(dto.getUsername(), refreshToken);

        return ResponseEntity.ok(SuccessResponse.success(new TokenResponse(accessToken, refreshToken)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRequestDto request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);

        if (!refreshTokenService.isValidRefreshToken(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found or expired");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);
        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String username = jwtTokenProvider.getUsername(accessToken);

        // Redis RefreshToken 삭제
        refreshTokenService.deleteRefreshToken(username);
        // AccessToken 블랙리스트 등록
        long remaining = jwtTokenProvider.getRemainingValidity(accessToken);
        refreshTokenService.blacklistAccessToken(accessToken, remaining);
        return ResponseEntity.ok(SuccessResponse.success("로그아웃 되었습니다."));
    }

}
