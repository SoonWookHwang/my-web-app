package com.example.mywebapp.security;

import com.example.mywebapp.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity // Spring Security에 대한 디버깅 모드를 사용하기 위한 어노테이션 (default : false)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtTokenProvider jwtTokenProvider;

  private final RefreshTokenRepository refreshTokenRepository;

  @Autowired
  public SecurityConfiguration(JwtTokenProvider jwtTokenProvider,
      RefreshTokenRepository refreshTokenRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.httpBasic().disable() // REST API는 UI를 사용하지 않으므로 기본설정을 비활성화
        .csrf().disable() // REST API는 csrf 보안이 필요 없으므로 비활성화
        .sessionManagement()
        .sessionCreationPolicy(
            SessionCreationPolicy.STATELESS) // JWT Token 인증방식으로 세션은 필요 없으므로 비활성화
        .and()
        .authorizeRequests()
        .antMatchers("/member/noAuth/**").permitAll() // 가입 및 로그인 주소는 허용
        .antMatchers(HttpMethod.GET, "/contents/find/**").permitAll() //좋아요 싫어요 상위 컨텐츠 조회는 인증 생략

        .antMatchers("**exception**").permitAll()
        .antMatchers("/contents/status/**","/member/delete/**","/member/admin/**").hasRole("ADMIN")
        .anyRequest().access("hasAnyRole('USER', 'ADMIN')")
        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, refreshTokenRepository),
            UsernamePasswordAuthenticationFilter.class); // JWT Token 필터를 id/password 인증 필터 이전에 추가
  }


  @Override
  public void configure(WebSecurity webSecurity) {
    webSecurity.ignoring().antMatchers("/h2-console/**"); //h2-console 접근에 대한 필터 제외
  }
}
