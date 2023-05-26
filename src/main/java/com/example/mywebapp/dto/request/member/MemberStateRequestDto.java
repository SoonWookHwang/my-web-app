package com.example.mywebapp.dto.request.member;


import com.example.mywebapp.domain.member.enums.Adult;
import com.example.mywebapp.domain.member.enums.Dormant;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberStateRequestDto {

  @Enumerated(EnumType.STRING)
  private Dormant isDormant;

  @Enumerated(EnumType.STRING)
  private Adult isAdult;

}
