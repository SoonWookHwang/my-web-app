package com.example.mywebapp.service;


import com.example.mywebapp.dto.request.member.SignUpRequestDto;
import com.example.mywebapp.repository.MemberInfoRepository;
import com.example.mywebapp.repository.MemberRepository;
import com.example.mywebapp.repository.RefreshTokenRepository;
import com.example.mywebapp.security.JwtTokenProvider;
import java.util.Collections;
import java.util.NoSuchElementException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService implements MemberServiceAPI {

  private final MemberRepository memberRepository;
  private final MemberInfoRepository memberInfoRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

  @Override
  public String registerMember(SignUpRequestDto dto)
      throws IllegalArgumentException {
    LOGGER.info("[registerMember] 회원 가입 정보 전달");
    String username = dto.getUsername();
    String password = dto.getPassword();

    if (memberRepository.existsByUsername(dto.getUsername())) {
      throw new IllegalArgumentException("이미 존재하는 아이디입니다");
    }

    Member newMember;
    if (dto.getRole().equalsIgnoreCase("admin")) {
      newMember = Member.builder()
          .username(username)
          .password(passwordEncoder.encode(password))
          .roles(Collections.singletonList("ROLE_ADMIN"))
          .isDormant("1")
          .build();
    } else {
      newMember = Member.builder()
          .username(username)
          .password(passwordEncoder.encode(password))
          .roles(Collections.singletonList("ROLE_USER"))
          .isDormant("1")
          .build();
    }
    MemberInfo newMemberInfo = createMemberDetail();
    newMember.setMemberInfo(newMemberInfo);
    newMemberInfo.setMember(newMemberInfo, newMember);
    memberRepository.save(newMember);
    return "회원가입이 성공했습니다.";
  }

  public TokenDto login(LoginRequestDto dto, HttpServletResponse response) throws IllegalArgumentException {
    LOGGER.info("[login] 회원 정보 요청");
    Member member = memberRepository.findByUsername(dto.getUsername())
        .orElseThrow(() -> new IllegalArgumentException("회원정보가 없습니다"));

    LOGGER.info("[login] username : {}", dto.getUsername());

    LOGGER.info("[login] 패스워드 비교 수행");
    if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    LOGGER.info("[login] 패스워드 일치");

    String existRefreshToken = refreshTokenRepository.findRefreshTokenByUserId(member.getId());
    LOGGER.info("[login] DB에 refreshToken이 저장되어있는지 확인");

    if(existRefreshToken!=null) {
      refreshTokenRepository.deleteRefreshToken(existRefreshToken);
      LOGGER.info("[login] 기존 refreshToken 삭제");
    }

    String refreshToken = jwtTokenProvider.createRefreshToken();
    LOGGER.info("[login] refreshToken 생성");


    refreshTokenRepository.saveRefreshToken(refreshToken, member.getId(),60*60*24*30);
    LOGGER.info("[login] DB에 refreshToken 새롭게 저장");



    Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", refreshToken);
    refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setPath("/");
    response.addCookie(refreshTokenCookie);
    LOGGER.info("[login] 쿠키에 refreshToken 전달");

    TokenDto tokenDto = TokenDto.builder()
        .accessToken(jwtTokenProvider.createToken(member.getUsername(), member.getRoles()))
        .build();
    LOGGER.info("[login] 로그인 성공 TokenDto 생성");
    return tokenDto;
  }

  @Override
  public MemberResponseEntity<MemberInfo> updateMemberInfo(MemberDetailsRequestDto dto) {
    Member member = loginUser();
    MemberInfo updateMemberInfo = memberInfoRepository.findById(member.getId())
        .orElseThrow(() -> new IllegalArgumentException("회원정보가 없습니다."));
    updateMemberInfo.update(dto, updateMemberInfo);
    return MemberResponseEntity.success(updateMemberInfo);
  }

  @Override
  public MemberResponseEntity<Member> isDormant(String isDormant) {
    Member loginUser = loginUser();
    loginUser.changeDormantStat(loginUser, Dormant.valueOf(isDormant));

    return MemberResponseEntity.success(loginUser);
  }

  @Override
  public MemberResponseEntity<Member> isAdult(String isAdult)
      throws IllegalArgumentException {
    log.info("[성인 계정 설정 서비스 진입]");
    Member loginUser = loginUser();
    MemberInfo loginUserInfo = memberInfoRepository.findById(loginUser.getId()).orElse(null);
    assert loginUserInfo != null;
    if (loginUserInfo.getAge() < 18) {
      throw new IllegalArgumentException("성인 나이 기준에 충족하지 못합니다.");
    }
    loginUser.changeAdultStat(loginUser, Adult.valueOf(isAdult));
    return MemberResponseEntity.success(loginUser);
  }

  @Override
  public MemberResponseEntity<String> deleteMember(Long memberId) {
    Member target = memberRepository.findById(memberId)
        .orElseThrow(() -> new NoSuchElementException("회원정보가 없습니다"));
    memberRepository.delete(target);
    return MemberResponseEntity.success("회원 삭제 성공");
  }

  public MemberInfo createMemberDetail() {
    MemberInfo newMemberInfo = MemberInfo.builder()
        .email("미설정")
        .age(0)
        .gender("3")
        .build();
    return memberInfoRepository.save(newMemberInfo);
  }

  public Member loginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Member loginuser = memberRepository.findByUsername(authentication.getName()).orElseThrow();
    return loginuser;
  }
}
