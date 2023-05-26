package com.example.mywebapp.repository;

import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsRepsitory extends JpaRepository<Contents, Long> {

  Page<Contents> findAll(Pageable pageable);

  Page<Contents> findAllByMember(Pageable pageable, Member member);
}
