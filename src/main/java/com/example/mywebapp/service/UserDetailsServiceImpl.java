package com.example.mywebapp.service;

import com.example.mywebapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    LOGGER.info("[loadUserByUsername] loadUserByUsername 수행. username : {}", username);
    return memberRepository.findByUsername(username).orElse(null);
  }

}