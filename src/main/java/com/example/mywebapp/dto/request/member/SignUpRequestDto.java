package com.example.mywebapp.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpRequestDto {

  private String username;
  private String password;
  private String role;

}
