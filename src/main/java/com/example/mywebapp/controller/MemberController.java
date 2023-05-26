package com.example.mywebapp.controller;

import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.domain.member.MemberInfo;
import com.example.mywebapp.dto.request.member.LoginRequestDto;
import com.example.mywebapp.dto.request.member.MemberDetailsRequestDto;
import com.example.mywebapp.dto.request.member.SignUpRequestDto;
import com.example.mywebapp.dto.response.MemberResponseEntity;
import com.example.mywebapp.service.ContentsHistoryService;
import com.example.mywebapp.service.MemberService;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController implements MemberControllerAPI {

  private final MemberService memberService;

  private final ContentsHistoryService contentsHistoryService;


  @Override
  @PostMapping("/member/noAuth/register")
  public MemberResponseEntity<String> registerMember(@RequestBody SignUpRequestDto dto) {
    try {
      return MemberResponseEntity.success(memberService.registerMember(dto));
    } catch (IllegalArgumentException e) {
      return MemberResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @PostMapping("/member/noAuth/login")
  public MemberResponseEntity<?> login(@RequestBody LoginRequestDto requestDto,
      HttpServletResponse response) {

    log.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", requestDto.getUsername());
    try {
      return MemberResponseEntity.success(memberService.login(requestDto, response));
    } catch (Exception e) {
      return MemberResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @GetMapping("/member/admin/adult")
  public MemberResponseEntity<Page<Member>> getUsersWhoReadAdultContent() {
    return MemberResponseEntity.success(contentsHistoryService.getUsersWhoReadAdultContent());
  }

  @GetMapping("/member/admin/{contentsId}")
  public MemberResponseEntity<?> getUsersWhoReadContent(@PathVariable Long contentsId) {
    try {
      return MemberResponseEntity.success(contentsHistoryService.findMembersByContent(contentsId));
    } catch (Exception e) {
      return MemberResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @Override
  @PutMapping("/member/info")
  public MemberResponseEntity<MemberInfo> updateMemberInfo(
      @RequestBody MemberDetailsRequestDto dto) {
    try {
      return memberService.updateMemberInfo(dto);
    } catch (Exception e) {
      return MemberResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @Override
  @PutMapping("/member/dormant")
  public MemberResponseEntity<Member> isDormant(@RequestParam String isDormant) {
    try {
      return memberService.isDormant(isDormant);
    } catch (Exception e) {
      return MemberResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @Override
  @PutMapping("/member/adult")
  public MemberResponseEntity<Member> isAdult(@RequestParam String isAdult) {
    log.info("[성인계정 설정 컨트롤러 진입]");
    try {
      return memberService.isAdult(isAdult);
    } catch (Exception e) {
      return MemberResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }


  @Override
  @DeleteMapping("/member/delete/{memberId}")
  public MemberResponseEntity<String> deleteMember(@PathVariable Long memberId) {
    try {
      return memberService.deleteMember(memberId);
    } catch (Exception e) {
      return MemberResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }


  @GetMapping("/error")
  public MemberResponseEntity<?> isNotAdmin() {
    return MemberResponseEntity.fail(HttpStatus.BAD_REQUEST, "관리자만 사용가능한 기능입니다");
  }
}
