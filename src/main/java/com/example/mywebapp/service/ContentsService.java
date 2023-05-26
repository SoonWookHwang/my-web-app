package com.example.mywebapp.service;


import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.dto.request.contents.ContentsRequestDto;
import com.example.mywebapp.repository.ContentsRepsitory;
import com.example.mywebapp.repository.MemberRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentsService {

  private final ContentsRepsitory contentsRepsitory;
  private final MemberRepository memberRepository;

  private final ContentsHistoryService contentsHistoryService;

  public Contents createContents(ContentsRequestDto dto) {
    Member uploader = loginUser();
    Contents newContents = Contents.builder()
        .title(dto.getTitle())
        .author(dto.getAuthor())
        .isFree(dto.getIsFree().status)
        .isAdult(dto.getIsAdult())
        .member(uploader)
        .liked(0)
        .hated(0)
        .build();
    return contentsRepsitory.save(newContents);
  }


  @Transactional
  public Contents isFreeContents(String isFree, Long contentsId) {
    Contents targetContents = contentsRepsitory.findById(contentsId)
        .orElseThrow(() -> new IllegalArgumentException("해당 자료가 없습니다."));
    targetContents.isFree(targetContents, ContentsType.valueOf(isFree));
    return targetContents;
  }

  @Transactional
  public Contents setCoin(int setCoin, Long contentsId) {
    Contents targetContents = contentsRepsitory.findById(contentsId)
        .orElseThrow(() -> new NoSuchElementException("해당 자료가 없습니다"));
    if (setCoin < 100 || setCoin > 500) {
      throw new IllegalArgumentException("가격 설정 가능 범위는 100원에서 500원 사이입니다.");
    }
    targetContents.setCoin(targetContents, setCoin);
    targetContents.isFree(targetContents, ContentsType.PAYED);
    return targetContents;
  }

  public Page<Contents> getAllMyContents() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    return contentsRepsitory.findAllByMember(pageable, loginUser());
  }

  @Transactional
  public Contents readContent(Long contentsId) {
    Contents contents = contentsRepsitory.findById(contentsId)
        .orElseThrow(() -> new NoSuchElementException("해당 컨텐츠가 존재하지 않습니다"));
    Member user = loginUser();
    if (contents.getIsAdult().equals("2")) {
      user.setAdultViewCnt(user.getAdultViewCnt() + 1);
    }
    contentsHistoryService.addContentHistory(user, contents);
    return contents;
  }

  public Page<Contents> mostThreeLiked() {
    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "liked"));
    return contentsRepsitory.findAll(pageable);
  }


  public Page<Contents> mostThreeHated() {
    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "hated"));
    return contentsRepsitory.findAll(pageable);
  }

  public Member loginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Member loginUser = memberRepository.findByUsername(authentication.getName()).orElseThrow();
    return loginUser;
  }
}

