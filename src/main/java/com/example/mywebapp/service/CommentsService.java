package com.example.mywebapp.service;


import com.example.mywebapp.domain.comment.Comments;
import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.dto.request.contents.CommentRequestDto;
import com.example.mywebapp.repository.CommentsRepository;
import com.example.mywebapp.repository.ContentsRepsitory;
import com.example.mywebapp.repository.MemberRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentsService {

  private final CommentsRepository commentsRepository;
  private final MemberRepository memberRepository;
  private final ContentsRepsitory contentsRepository;

  public Comments createComments(Long contentsId, CommentRequestDto dto) {
    if (dto.containsSpecialCharacter(dto)) {
      throw new IllegalArgumentException("댓글은 특수문자를 사용할 수 없습니다.");
    }
    Contents targetContents = contentsRepository.findById(contentsId)
        .orElseThrow(() -> new NoSuchElementException("해당자료가 존재하지 않습니다"));

    Member member = loginUser();

    Comments newComments = new Comments(dto.getComments(), member, targetContents);
    return commentsRepository.save(newComments);
  }

  @Transactional
  public Comments updateComments(Long commentsId, CommentRequestDto dto)
      throws IllegalAccessException {
    if (dto.containsSpecialCharacter(dto)) {
      throw new IllegalArgumentException("댓글은 특수문자를 사용할 수 없습니다.");
    }
    Member loginUser = loginUser();

    Comments targetComments = commentsRepository.findById(commentsId)
        .orElseThrow(()->new NoSuchElementException("해당 댓글이 존재하지 않습니다."));

    if(!loginUser.getUsername().equals(targetComments.getMember().getUsername())){
      throw new IllegalAccessException("작성자가 아니면 수정이 불가능합니다");
    }
    targetComments.update(targetComments, dto);

    return targetComments;
  }

  public Member loginUser(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Member loginUser = memberRepository.findByUsername(authentication.getName()).orElseThrow();
    return loginUser;
  }
}