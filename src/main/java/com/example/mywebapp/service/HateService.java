package com.example.mywebapp.service;

import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.like.Hates;
import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.repository.ContentsRepsitory;
import com.example.mywebapp.repository.HatesRepository;
import com.example.mywebapp.repository.LikesRepository;
import com.example.mywebapp.repository.MemberRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HateService {

  private final HatesRepository hatesRepository;
  private final MemberRepository memberRepository;

  private final ContentsRepsitory contentsRepsitory;
  private final LikesRepository likesRepository;

  @Transactional
  public String hateContents(Long contentsId) {
    Contents contents = contentsRepsitory.findById(contentsId)
        .orElseThrow(NoSuchElementException::new);

    Member member = loginUser();

    if (hatesRepository.findByMemberAndContents(member, contents) == null) {
      if (likesRepository.findByMemberAndContents(member, contents) != null) {
        return "작품당 좋아요와 싫어요는 하나만 등록가능합니다.";
      }
      contents.setHated(contents.getHated() + 1);
      Hates hateContents = new Hates(contents, member); // true 처리
      hatesRepository.save(hateContents);
      return "싫어요 처리 완료";

    } else {
      Hates hateContents = hatesRepository.findByMemberAndContents(member, contents);
      hateContents.unHateContents(contents);
      hatesRepository.delete(hateContents);
      return "싫어요 취소";
    }
  }

  public Member loginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Member loginUser = memberRepository.findByUsername(authentication.getName()).orElseThrow();
    return loginUser;
  }

  ;
}

