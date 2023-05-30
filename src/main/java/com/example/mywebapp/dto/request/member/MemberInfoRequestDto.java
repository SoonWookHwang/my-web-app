package com.example.mywebapp.dto.request.member;

import com.example.mywebapp.domain.member.enums.Gender;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
@Setter
public class MemberInfoRequestDto {

  private String email;

  private int age;

  @Enumerated(EnumType.STRING)
  private Gender gender;


}
