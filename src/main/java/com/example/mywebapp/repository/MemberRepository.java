package com.example.mywebapp.repository;

import com.example.mywebapp.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByUsername(String username);

  boolean existsByUsername(String username);

  Page<Member> findAll(Pageable pageable);

  Page<Member> findByIdIn(Pageable pageable, List<Long> ids);

  @Modifying
  @Query("update Member u set u.adultViewCnt = 0")
  void resetViewCnt();
}
