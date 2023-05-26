package com.example.mywebapp.domain.member.enums;

import lombok.Getter;

@Getter
public enum Gender {

  MALE("1"),

  FEMALE("2"),

  NOT_SPECIFIED("3"),
  ;

  public final String gender;

  Gender(String gender) {
    this.gender = gender;
  }

}
