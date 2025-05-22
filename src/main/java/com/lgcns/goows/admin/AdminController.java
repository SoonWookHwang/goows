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
import com.lgcns.goows.service.AdminMemberService;
import com.lgcns.goows.service.MemberService;
import com.lgcns.goows.service.RefreshTokenService;
import com.lgcns.goows.service.ServerStatusService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final ServerStatusService serverStatusService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final AdminMemberService adminMemberService;

    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin-login";
    }

    @GetMapping("/status")
    public String statusPage(Model model) {
        model.addAttribute("status", serverStatusService.getStatus() ? "ACTIVE" : "INACTIVE");
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
    public ResponseEntity<?> checkAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null
                || userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(HttpStatus.FORBIDDEN, "접근 권한 없음"));
        }
        return ResponseEntity.ok(SuccessResponse.success("어드민 계정 확인"));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody MemberLoginDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                dto.getUsername(), dto.getPassword());
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

    @GetMapping("/members")
    public String adminMemberPage(Model model) {
        List<Member> members = adminMemberService.findAllMembers();
        model.addAttribute("members", members);
        return "admin-member";
    }

    @PostMapping("/members/updateStatus/{memberId}")
    @ResponseBody // 이 메서드는 JSON 응답을 반환할 것이므로 @ResponseBody 추가
    public String updateMemberActiveStatus(
            @PathVariable("memberId") Long memberId,
            @RequestBody Map<String, Boolean> payload, // JSON 객체를 Map으로 받음
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        try {
            log.info(userDetailsImpl.getMember().getUsername());
            // isInactive가 true이면 비활성화, false이면 활성화
            boolean isInactive = payload.get("isInactive");// 서버에서 실제 isActive 값 계산
            boolean newIsActive = !isInactive;

            adminMemberService.setMemberActiveStatus(memberId, newIsActive);

            redirectAttributes.addFlashAttribute("message",
                    "회원 ID: " + memberId + " 의 상태가 " + (newIsActive ? "활성화" : "비활성화") + "되었습니다.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "오류: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 상태 변경 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/admin/members";
    }

}