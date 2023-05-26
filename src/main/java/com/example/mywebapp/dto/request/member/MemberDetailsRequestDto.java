package com.example.mywebapp.dto.request.member;

import com.example.mywebapp.domain.member.enums.Gender;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDetailsRequestDto {

  private String email;

  private int age;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private String type;


}
