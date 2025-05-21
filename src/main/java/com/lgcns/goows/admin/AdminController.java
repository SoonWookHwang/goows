package com.lgcns.goows.admin;

import com.lgcns.goows.dto.MemberLoginDto;
import com.lgcns.goows.dto.MemberResponseDto;
import com.lgcns.goows.entity.Member;
import com.lgcns.goows.global.common.ErrorResponse;
import com.lgcns.goows.global.common.SuccessResponse;
import com.lgcns.goows.global.common.TokenResponse;
import com.lgcns.goows.global.security.JwtTokenProvider;
import com.lgcns.goows.global.security.UserDetailsImpl;
import com.lgcns.goows.mappers.MemberMapper;
import com.lgcns.goows.service.MemberService;
import com.lgcns.goows.service.RefreshTokenService;
import com.lgcns.goows.service.ServerStatusService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ServerStatusService serverStatusService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin-login";
    }
    @GetMapping("/status")
    public String statusPage(Model model) {
        model.addAttribute("status",serverStatusService.getStatus()? "ACTIVE" : "INACTIVE");
        return "admin-status";
    }
    @PostMapping("/toggle-status")
    @ResponseBody
    public ResponseEntity<?> toggleServerStatus() {
        serverStatusService.toggleStatus();
        return ResponseEntity.ok(SuccessResponse.success("서버상태 변경"));
    }

    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<?> checkAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null || userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(HttpStatus.FORBIDDEN, "접근 권한 없음"));
        }
        return ResponseEntity.ok(SuccessResponse.success("어드민 계정 확인"));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody MemberLoginDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.generateAccessToken(dto.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(dto.getUsername());

        refreshTokenService.saveRefreshToken(dto.getUsername(), refreshToken);
        Member member = memberService.getMemberByUsername(dto.getUsername());
        MemberResponseDto userInfo = MemberMapper.toMemberResponseDto(member);

        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("userInfo", userInfo);
        userInfoMap.put("tokenInfo", new TokenResponse(accessToken, refreshToken));

        return ResponseEntity.ok(SuccessResponse.success(userInfoMap));
    }

    @PostMapping("/logout")
    @ResponseBody
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