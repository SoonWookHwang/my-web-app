package com.example.mywebapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.content.ContentsType;
import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.dto.request.contents.ContentsRequestDto;
import com.example.mywebapp.dto.request.member.LoginRequestDto;
import com.example.mywebapp.dto.request.member.SignUpRequestDto;
import com.example.mywebapp.repository.ContentsRepsitory;
import com.example.mywebapp.repository.MemberRepository;
import com.example.mywebapp.repository.RefreshTokenRepository;
import com.example.mywebapp.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
class ContentsServiceTest {

  @Autowired
  @Mock
  ContentsRepsitory contentsRepsitory;
  @Autowired
  @Mock
  MemberRepository memberRepository;
  @Autowired
  @Mock
  ContentsHistoryService contentsHistoryService;
  @Autowired
  @Mock
  MemberService memberService;
  @Autowired
  @Mock
  JwtTokenProvider jwtTokenProvider;
  @Autowired
  @Mock
  ContentsService contentsService;
  @Autowired
  @Mock
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

    MockHttpServletResponse response = new MockHttpServletResponse();
    String accessToken = memberService.login(LoginRequestDto.builder()
        .username("유저-테스트아이디")
        .password("test123!@").build(), response).getAccessToken();

    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }


  @DisplayName("컨텐츠생성")
  @Nested
  class 컨텐츠생성 {

    @Test
    @DisplayName("컨텐츠 생성 성공 시 생성된 컨텐츠 객체를 리턴한다.")
    void createContents() {
      ContentsRequestDto testDto = ContentsRequestDto.builder()
          .title("test_title")
          .isFree(ContentsType.FREE)
          .isAdult("2")
          .author("test_author").build();

      String resultTitle = contentsService.createContents(testDto).getTitle();
      String resultIsFree = contentsService.createContents(testDto).getIsFree();
      String resultIsAdult = contentsService.createContents(testDto).getIsAdult();
      String resultAuthor = contentsService.createContents(testDto).getAuthor();
      int resultLiked = contentsService.createContents(testDto).getLiked();

      assertEquals("test_title", resultTitle);
      assertEquals("1", resultIsFree);
      assertEquals("2", resultIsAdult);
      assertEquals("test_author", resultAuthor);
      assertEquals(0, resultLiked);
    }
  }

  @Nested
  @DisplayName("컨텐츠 상태 변경할 때")
  class 컨텐츠상태변경 {
    @Test
    @DisplayName("무료로 변경하면 코인은 0으로 변하고 isFree는 1로 변경된다.")
    void isFreeContents() {
      Member member = Member.builder()
          .id(1L)
          .username("testUser")
          .password("test123!@").build();
      Contents contents = Contents.builder()
          .title("testContents")
          .isFree("2")
          .isAdult("1")
          .author("testAuthor")
          .member(member)
          .build();
      contents.setId(1L);
      contents.setCoin(contents,200);
      contentsRepsitory.save(contents);
      Contents changed = contentsRepsitory.findById(1L).orElse(null);
      contentsService.isFreeContents("FREE",1L);

      assert changed != null;
      assertEquals("1",changed.getIsFree());
      assertEquals(0,changed.getCoin());
    }

    @Test
    @DisplayName("컨텐츠 가격 설정을 하면 코인의 값으로 변경되고 isFree는 2로 변경된다.")
    void setCoin() {
      Member member = Member.builder()
          .id(1L)
          .username("testUser")
          .password("test123!@").build();
      Contents contents = Contents.builder()
          .title("testContents")
          .isFree("1")
          .isAdult("1")
          .author("testAuthor")
          .member(member)
          .build();
      contents.setId(1L);
      contentsRepsitory.save(contents);
      Contents changed = contentsRepsitory.findById(1L).orElse(null);

      contentsService.setCoin(200, 1L);
      assert changed != null;
      assertEquals("2", changed.getIsFree());
      assertEquals(200, changed.getCoin());
    }
  }

  @Test
  void getAllMyContents() {

  }

  @Test
  void readContent() {
  }

  @Test
  void mostThreeLiked() {
  }

  @Test
  void mostThreeHated() {
  }

}