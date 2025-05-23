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
@RequestMapping("/mypage/modify")
public class MypageController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> postModifyMemberInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody MemberModifyDto dto) {
        try {
            String username = userDetails.getUsername();

            boolean nicknameProvided = dto.getNickname() != null;
            boolean passwordProvided = dto.getCurrentPassword() != null || dto.getNewPassword() != null;

            if (nicknameProvided && !passwordProvided) {
                // 닉네임 변경 요청
                memberService.modifyMemberInfo(username, dto);
            } else if (!nicknameProvided && passwordProvided) {
                // 비밀번호 변경 요청
                memberService.modifyMemberInfo(username, dto);
            } else {
                // 잘못된 요청: 닉네임과 비밀번호 정보가 동시에 있거나, 둘 다 없는 경우
                return ResponseEntity.badRequest().body("잘못된 요청입니다. 닉네임 또는 비밀번호 변경 정보 중 하나만 포함해주세요.");
            }
            return ResponseEntity.ok("정보가 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정에 실패했습니다: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
