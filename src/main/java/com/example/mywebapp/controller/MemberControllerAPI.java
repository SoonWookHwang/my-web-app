package com.example.mywebapp.controller;


import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.domain.member.MemberInfo;
import com.example.mywebapp.dto.request.member.MemberDetailsRequestDto;
import com.example.mywebapp.dto.request.member.SignUpRequestDto;
import com.example.mywebapp.dto.response.MemberResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MemberControllerAPI {


  // 회원가입
  @PostMapping
  MemberResponseEntity<String> registerMember(@RequestBody SignUpRequestDto dto);

  /* 로그인
   *  스프링 시큐리티로 설정
   */

  // 회원정보수정
  @PutMapping
  MemberResponseEntity<MemberInfo> updateMemberInfo(MemberDetailsRequestDto dto);

  // 휴면계정 설정
  @PutMapping
  MemberResponseEntity<Member> isDormant(String isDormant);

  // 성인인증 상태 변경
  @PutMapping
  MemberResponseEntity<Member> isAdult(String isAdult);

  // 계정 삭제
  @DeleteMapping
  MemberResponseEntity<String> deleteMember(@PathVariable Long memberId);

}
