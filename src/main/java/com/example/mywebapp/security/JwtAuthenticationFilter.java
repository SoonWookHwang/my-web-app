package com.example.mywebapp.security;

import com.example.mywebapp.repository.RefreshTokenRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
      RefreshTokenRepository refreshTokenRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest servletRequest,
      HttpServletResponse servletResponse,
      FilterChain filterChain) throws ServletException, IOException {

    String token = jwtTokenProvider.resolveToken(servletRequest);
    LOGGER.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

    String refreshToken = jwtTokenProvider.resolveRefreshToken(servletRequest);
    LOGGER.info("[doFilterInternal] refreshToken 값 추출 완료. refreshToken : {}", refreshToken);


    LOGGER.info("[doFilterInternal] token 값 유효성 체크 시작");
    if (token != null && jwtTokenProvider.validateToken(token)) {
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      LOGGER.info("[doFilterInternal] token 값 유효성 체크 완료");
    } else if (refreshToken != null) {
      if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
        String newAccessToken = jwtTokenProvider.generateAccessTokenFromRefreshToken(refreshToken);
        jwtTokenProvider.addAccessTokenToResponse(newAccessToken, servletResponse);
        LOGGER.info("[doFilterInternal] 새로운 엑세스 토큰 발급 완료");
      } else {
        LOGGER.info("[doFilterInternal] 리프레시 토큰이 만료되었거나 유효하지 않습니다.");
        SecurityContextHolder.clearContext();
        refreshTokenRepository.deleteRefreshToken(refreshToken);
        servletResponse.sendError(401, "재 로그인이 필요합니다.");
        return;
      }

    // 엑세스 토큰이 만료되었을 때 리프레시 토큰을 사용하여 새로운 엑세스 토큰 발급
//    if (!jwtTokenProvider.validateToken(token)) {
//      if (refreshToken != null && jwtTokenProvider.validateRefreshToken(refreshToken)) {
//        String newAccessToken = jwtTokenProvider.generateAccessTokenFromRefreshToken(refreshToken);
//
//        // 새로운 엑세스 토큰을 응답 헤더에 추가
//        jwtTokenProvider.addAccessTokenToResponse(newAccessToken, servletResponse);
//
//      } else {
//        LOGGER.info("[doFilterInternal] 리프레시 토큰이 만료되었거나 유효하지 않습니다.");
//        // 인증 정보 초기화
//        SecurityContextHolder.clearContext();
//        refreshTokenRepository.deleteRefreshToken(refreshToken);
//        servletResponse.sendError(401,"재 로그인이 필요합니다.");
//      }
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }
}
