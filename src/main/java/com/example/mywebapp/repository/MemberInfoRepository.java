package com.example.mywebapp.repository;

import com.example.mywebapp.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

}
