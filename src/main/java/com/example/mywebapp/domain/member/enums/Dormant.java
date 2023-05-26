package com.example.mywebapp.domain.member.enums;

import lombok.Getter;

@Getter
public enum Dormant {

  ACTIVE("1"),

  IN_ACTIVE("2");

  //휴면 계정 관련 상태 추가 가능

  public final String dormant;

  Dormant(String dormant) {
    this.dormant = dormant;
  }

}
