package com.example.mywebapp.domain.member;

import com.example.mywebapp.domain.Timestamped;
import com.example.mywebapp.dto.request.member.MemberInfoRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberInfo extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String email;

  @Column
  private int age;

  @Column
  private String gender;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  @JsonIgnore
  private Member member;

  public void update(MemberInfoRequestDto dto, MemberInfo target) {
    String email = dto.getEmail();
    int age = dto.getAge();
    String gender = dto.getGender().gender;

    if (!email.isEmpty() && !email.isBlank()) {
      target.setEmail(email);
    }
    if (age < 14) {
      throw new IllegalArgumentException("만 14세 이하로는 설정할수 없습니다.");
    } else {
      target.setAge(age);
    }
    if (!gender.isEmpty() && !gender.isBlank()) {
      target.setGender(gender);
    }
  }
  @Builder
  public MemberInfo(String email, int age, String gender) {
    this.email = email;
    this.age = age;
    this.gender = gender;
  }

  public void setMember(MemberInfo memberInfo, Member member) {
    memberInfo.setMember(member);
  }
}
