package com.example.mywebapp.repository;

import com.example.mywebapp.domain.content.Contents;
import com.example.mywebapp.domain.like.Likes;
import com.example.mywebapp.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

  Likes findByMemberAndContents(Member member, Contents contents);


}
