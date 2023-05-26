package com.example.mywebapp.service;

import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.domain.member.MemberInfo;
import com.example.mywebapp.dto.request.member.MemberDetailsRequestDto;
import com.example.mywebapp.dto.request.member.SignUpRequestDto;
import com.example.mywebapp.dto.response.MemberResponseEntity;

public interface MemberServiceAPI {

  String registerMember(SignUpRequestDto dto);

  // 회원정보수정
  MemberResponseEntity<MemberInfo> updateMemberInfo(MemberDetailsRequestDto dto);

  // 휴면계정 설정
  MemberResponseEntity<Member> isDormant(String isDormant);

  MemberResponseEntity<Member> isAdult(String isAdult) throws IllegalAccessException;

  // 계정 삭제
  MemberResponseEntity<?> deleteMember(Long memberId);


}
