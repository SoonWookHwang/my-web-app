package com.example.mywebapp.service;

import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.history.ContentsHistory;
import com.example.mywebapp.domain.member.Member;
import com.example.mywebapp.repository.ContentsHistoryRepository;
import com.example.mywebapp.repository.ContentsRepsitory;
import com.example.mywebapp.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentsHistoryService {

  private final MemberRepository memberRepository;
  private final ContentsHistoryRepository contentsHistoryRepository;

  private final ContentsRepsitory contentsRepsitory;

  public Page<Member> getUsersWhoReadAdultContent() {
    LocalDateTime startDate = LocalDateTime.now().minusDays(7);
    List<Long> userIds = contentsHistoryRepository.findMembersWhoReadAdultContent(startDate);
    Pageable pageable = PageRequest.of(0, 10);
    return memberRepository.findByIdIn(pageable, userIds);
  }

  public Page<ContentsHistory> findMembersByContent(Long contentsId) {
    Contents contents = contentsRepsitory.findById(contentsId)
        .orElseThrow(() -> new IllegalArgumentException("해당 컨텐츠가 없습니다."));
    Pageable pageable = PageRequest.of(0, 10);
    return contentsHistoryRepository.findMembersByContent(pageable, contents);
  }

  public void addContentHistory(Member member, Contents contents) {
    ContentsHistory contentsHistory = new ContentsHistory();
    contentsHistory.setMember(member);
    contentsHistory.setContents(contents);
    contentsHistoryRepository.save(contentsHistory);
  }
}
