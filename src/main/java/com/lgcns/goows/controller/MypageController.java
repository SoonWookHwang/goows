package com.lgcns.goows.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.goows.dto.MemberModifyDto;
import com.lgcns.goows.service.MemberService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MemberService memberService;

    @PostMapping("/modify")
    public ResponseEntity<?> postModifyMemberInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody MemberModifyDto dto,
                                                    @RequestParam("action") String action) {
        try {
            // String username = userDetails.getUsername();
            String username = "구스";

            if ("nickname".equals(action)) {
                // 닉네임 변경 요청 시 비밀번호 관련 필드가 있으면 잘못된 요청으로 처리
                if (dto.getCurrentPassword() != null || dto.getNewPassword() != null) {
                    return ResponseEntity.badRequest().body("잘못된 요청입니다.");
                }
                memberService.modifyMemberInfo(username, dto);
                return ResponseEntity.ok("닉네임이 성공적으로 수정되었습니다.");
            } else if ("password".equals(action)) {
                // 비밀번호 변경 요청 시 닉네임 필드가 있으면 잘못된 요청으로 처리
                if (dto.getNickname() != null) {
                    return ResponseEntity.badRequest().body("잘못된 요청입니다: 비밀번호 변경 시에는 닉네임 정보를 포함할 수 없습니다.");
                }
                memberService.modifyMemberInfo(username, dto);
                return ResponseEntity.ok("비밀번호가 성공적으로 수정되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("잘못된 요청입니다: 'action' 파라미터를 확인해주세요 (nickname 또는 password).");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정에 실패했습니다: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
