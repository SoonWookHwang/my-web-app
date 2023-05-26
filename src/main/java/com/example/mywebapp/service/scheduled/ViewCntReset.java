package com.example.mywebapp.service.scheduled;

import com.example.mywebapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCntReset {

  private final MemberRepository memberRepository;


  @Scheduled(cron = "0 0 0 * * SUN") // 매주 일요일 0시 0분에 실행
  @Transactional
  public void resetViewCnt() {
    try {
      memberRepository.resetViewCnt();
    } catch (Exception e) {
      log.info("view cnt를 초기화하는데 문제가 발생하였습니다. 원인 : {}", e.getMessage());
    }
  }
}
