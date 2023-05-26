package com.example.mywebapp.repository;

import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.like.Hates;
import com.example.mywebapp.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HatesRepository extends JpaRepository<Hates, Long> {

  Hates findByMemberAndContents(Member member, Contents contents);


}
