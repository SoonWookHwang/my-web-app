package com.example.mywebapp.service;

import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.like.Likes;
import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.repository.ContentsRepsitory;
import com.example.mywebapp.repository.HatesRepository;
import com.example.mywebapp.repository.LikesRepository;
import com.example.mywebapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

  private final LikesRepository likesRepository;
  private final MemberRepository memberRepository;

  private final ContentsRepsitory contentsRepsitory;

  private final HatesRepository hatesRepository;

  @Transactional
  public String likeContents(Long contentsId) {
    Contents contents = contentsRepsitory.findById(contentsId)
        .orElseThrow(() -> new IllegalArgumentException("해당 컨텐츠가 존재하지 않습니다."));

    Member member = loginUser();

    if (likesRepository.findByMemberAndContents(member, contents) == null) {
      if (hatesRepository.findByMemberAndContents(member, contents) != null) {
        return "작품당 좋아요와 싫어요는 하나만 등록가능합니다.";
      }
      contents.setLiked(contents.getLiked() + 1);
      Likes likeContents = new Likes(contents, member); // true 처리
      likesRepository.save(likeContents);
      return "좋아요 처리 완료";
    } else {
      Likes likeContents = likesRepository.findByMemberAndContents(member, contents);
      likeContents.unLikeContents(contents);
      likesRepository.delete(likeContents);
      return "좋아요 취소";
    }
  }

  public Member loginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Member loginuser = memberRepository.findByUsername(authentication.getName()).orElseThrow();
    return loginuser;
  }
}