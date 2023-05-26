package com.example.mywebapp.service;

import com.example.mywebapp.dto.TokenDto;
import com.example.mywebapp.dto.request.member.LoginRequestDto;
import com.example.mywebapp.dto.request.member.SignUpRequestDto;
import com.example.mywebapp.repository.MemberInfoRepository;
import com.example.mywebapp.repository.MemberRepository;
import com.example.mywebapp.repository.RefreshTokenRepository;
import com.example.mywebapp.security.JwtTokenProvider;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {
  @Autowired MemberService memberService;
  @Autowired MemberRepository memberRepository;
  @Autowired MemberInfoRepository memberInfoRepository;
  @Autowired JwtTokenProvider jwtTokenProvider;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired RefreshTokenRepository refreshTokenRepository;


  @BeforeEach
  void setUp() {
    String userUsername = "유저-테스트아이디";
    String userPassword = "test123!@";
    String userRole = "USER";
    SignUpRequestDto mockUserDto =
        SignUpRequestDto.builder()
            .username(userUsername)
            .password(userPassword)
            .role(userRole)
            .build();
    String adminUsername = "어드민-테스트아이디";
    String adminPassword = "test123!@";
    String adminRole = "ADMIN";

    SignUpRequestDto mockAdminDto =
        SignUpRequestDto.builder()
            .username(adminUsername)
            .password(adminPassword)
            .role(adminRole)
            .build();
    memberService.registerMember(mockUserDto);
    memberService.registerMember(mockAdminDto);
  }


  @Nested
  @DisplayName("회원가입 서비스")
  class 회원가입서비스 {

    @Nested
    @DisplayName("회원가입을 시도할 때")
    class 회원가입시도 {

      String username = "아이디1";
      String password = "test123!@";
      String role = "USER";

      SignUpRequestDto requestDto =
          SignUpRequestDto.builder()
              .username(username)
              .password(password)
              .role(role)
              .build();
      String username2 = "아이디2";
      String password2 = "패턴불일치패스워드";
      String role2 = "USER";

      SignUpRequestDto requestDto2 =
          SignUpRequestDto.builder()
              .username(username2)
              .password(password2)
              .role(role2)
              .build();
      String username3 = "아이디1";
      String password3 = "testpassword1!";
      String role3 = "USER";

      SignUpRequestDto requestDto3 =
          SignUpRequestDto.builder()
              .username(username3)
              .password(password3)
              .role(role3)
              .build();

      @Test
      @DisplayName("성공을 한다면 회원가입 성공 메세지를 리턴한다")
      void 회원가입성공() {

        String result = memberService.registerMember(requestDto);
        Assertions.assertEquals("회원가입이 성공했습니다.", result);
      }

      @Test
      @DisplayName("비밀번호 패턴이 안맞는 경우 IllegalArgumentException 에 실패 메세지를 포함해 throw 하고")
      void 비밀번호입력값오류() {
        try {
          memberService.registerMember(requestDto2);
        } catch (Exception e) {
          Assertions.assertThrows(IllegalArgumentException.class,
              () -> memberService.registerMember(requestDto2));
          Assertions.assertEquals("비밀번호 패턴이 올바르지 않습니다", e.getMessage());
        }
      }

      @Test
      @DisplayName("이미 존재하는 아이디로 시도한다면 IllegalArgumentException 에 실패 메세지를 포함해 throw 한다")
      void 존재하는아이디로회원가입() {

        try {
          memberService.registerMember(requestDto3);
        } catch (Exception e) {
          Assertions.assertThrows(IllegalArgumentException.class,
              () -> memberService.registerMember(requestDto3));
          Assertions.assertEquals("이미 존재하는 아이디입니다", e.getMessage());
        }
      }
    }
  }

  @Nested
  @DisplayName("로그인을 시도할 때")
  class login {
    @Test
    @DisplayName("성공 시 jwt 토큰을 tokenDto에 담아 리턴한다")
    void 로그인성공() {
      LoginRequestDto loginRequestDto = LoginRequestDto.builder()
          .username("유저-테스트아이디")
          .password("test123!@")
          .build();
      String resultToken = memberService.login(loginRequestDto,null).getAccessToken();

      Assertions.assertTrue(resultToken.startsWith("eyJhbGciOiJIUzI1NiJ9."));
      }
  }

  @Test
  void updateMemberInfo() {
  }

  @Test
  void isDormant() {
  }

  @Test
  void isAdult() {
  }

  @Test
  void deleteMember() {
  }

  @Test
  void createMemberDetail() {
  }

  @Test
  void loginUser() {
  }
}