package com.example.mywebapp.repository;

import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.history.ContentsHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentsHistoryRepository extends JpaRepository<ContentsHistory, Long> {

  @Query("SELECT ch.member.id FROM ContentsHistory ch INNER JOIN Contents c ON ch.contents.id = c.id WHERE c.isAdult = '2' AND ch.createdAt > ?1 GROUP BY ch.member.id HAVING COUNT(ch.member.id) >= 3")
  List<Long> findMembersWhoReadAdultContent(LocalDateTime startDate);

  @Query("SELECT ch FROM ContentsHistory ch WHERE ch.contents = :contents")
  Page<ContentsHistory> findMembersByContent(Pageable pageable,
      @Param("contents") Contents contents);
}