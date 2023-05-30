package com.example.mywebapp.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.mywebapp.domain.member.enums.Gender;
import com.example.mywebapp.dto.request.member.LoginRequestDto;
import com.example.mywebapp.dto.request.member.MemberInfoRequestDto;
import com.example.mywebapp.dto.request.member.SignUpRequestDto;
import com.example.mywebapp.repository.MemberInfoRepository;
import com.example.mywebapp.repository.MemberRepository;
import com.example.mywebapp.repository.RefreshTokenRepository;
import com.example.mywebapp.security.JwtTokenProvider;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  MemberInfoRepository memberInfoRepository;
  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  RefreshTokenRepository refreshTokenRepository;


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
        assertEquals("회원가입이 성공했습니다.", result);
      }

      @Test
      @DisplayName("비밀번호 패턴이 안맞는 경우 IllegalArgumentException에 실패 메세지를 포함해 throw 하고")
      void 비밀번호입력값오류() {
        try {
          memberService.registerMember(requestDto2);
        } catch (Exception e) {
          Assertions.assertThrows(IllegalArgumentException.class,
              () -> memberService.registerMember(requestDto2));
          assertEquals("비밀번호 패턴이 올바르지 않습니다", e.getMessage());
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
          assertEquals("이미 존재하는 아이디입니다", e.getMessage());
        }
      }
    }
  }

  @Nested
  @DisplayName("로그인을 시도할 때")
  class login {

    @Test
    @DisplayName("access토큰이 정상적으로 발행이되고")
    void 토큰발행() {
      List<String> roles = new ArrayList<>();
      roles.add("ROLE_USER");
      String createdToken = jwtTokenProvider.createToken("유저-테스트아이디", roles);
      Assertions.assertNotNull(createdToken);
    }

    @Test
    @DisplayName("로그인이 성공하면 엑세스 토큰을 Json body로 리턴하고 쿠키에 리프레쉬 토큰을 전달한다")
    void 로그인() {
      LoginRequestDto loginRequestDto = LoginRequestDto.builder()
          .username("유저-테스트아이디")
          .password("test123!@").build();
      MockHttpServletResponse response = new MockHttpServletResponse();
      String result = memberService.login(loginRequestDto, response).getAccessToken();
      String refreshToken = response.getCookie("REFRESH_TOKEN").getValue();
      assertNotNull(refreshToken);
      assertNotNull(result);
    }

    @Test
    @DisplayName("잘못된 정보로 로그인시도할 시 exception과 메시지를 리턴한다.")
    void 로그인실패() {
      LoginRequestDto requestDto = LoginRequestDto.builder()
          .username("유저-테스트아이디")
          .password("잘못된 비밀번호").build();
      String msg = "";
      MockHttpServletResponse response = new MockHttpServletResponse();
      try {
        memberService.login(requestDto, response);
      } catch (Exception e) {
        msg = msg += e.getMessage();
      }
      assertEquals("비밀번호가 일치하지 않습니다.", msg);
    }
  }

  @Test
  @DisplayName("멤버 인포 변경")
  void updateMemberInfo() {
    MemberInfoRequestDto dto = MemberInfoRequestDto.builder()
        .email("update@email.com")
        .age(20)
        .gender(Gender.MALE)
        .build();
    MockHttpServletResponse response = new MockHttpServletResponse();
    String accessToken = memberService.login(LoginRequestDto.builder()
        .username("유저-테스트아이디")
        .password("test123!@").build(), response).getAccessToken();

    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String email = memberService.updateMemberInfo(dto).getResult().getEmail();

    assertEquals("update@email.com", email);

  }

  @Test
  @DisplayName("멤버 휴면계정 설정")
  void isDormant() {
    String dto = "IN_ACTIVE";
    MockHttpServletResponse response = new MockHttpServletResponse();
    String accessToken = memberService.login(LoginRequestDto.builder()
        .username("유저-테스트아이디")
        .password("test123!@").build(), response).getAccessToken();

    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String dormantstat = memberService.isDormant(dto).getResult().getIsDormant();

    assertEquals("2", dormantstat);
  }


  @DisplayName("멤버 성인인증 설정")
  @Nested
  class 성인인증 {
    @Test
    @DisplayName("18세 이하일 때 실패 메시지를 리턴한다")
    void 성인인증실패() {
      String adult = "ADULT";
      String errMsg = "";
      MockHttpServletResponse response = new MockHttpServletResponse();
      String accessToken = memberService.login(LoginRequestDto.builder()
          .username("어드민-테스트아이디")
          .password("test123!@").build(), response).getAccessToken();

      Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      try {
        memberService.isAdult(adult);
      } catch (Exception e) {
        errMsg+=e.getMessage();
      }
      assertEquals("성인 나이 기준에 충족하지 못합니다.",errMsg);
    }
    @Test
    @DisplayName("성공시 휴면계정으로 전환된다.")
    void 성인인증성공() {
      String adult = "ADULT";
      MemberInfoRequestDto dto = MemberInfoRequestDto.builder()
          .email("update@email.com")
          .age(20)
          .gender(Gender.MALE)
          .build();

      MockHttpServletResponse response = new MockHttpServletResponse();
      String accessToken = memberService.login(LoginRequestDto.builder()
          .username("유저-테스트아이디")
          .password("test123!@").build(), response).getAccessToken();

      Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      memberService.updateMemberInfo(dto);
      String adultStat = memberService.isAdult(adult).getResult().getIsAdult();

      assertEquals("2", adultStat);
    }
  }

  @Test
  @DisplayName("멤버 삭제")
  void deleteMember() {
  }
}