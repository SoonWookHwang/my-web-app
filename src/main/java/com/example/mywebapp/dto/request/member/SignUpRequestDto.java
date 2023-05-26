package com.example.mywebapp.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SignUpRequestDto {

  private String username;
  private String password;
  private String role;

}
