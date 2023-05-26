package com.example.mywebapp.domain.member.enums;

import lombok.Getter;

@Getter
public enum Adult {

  NOT("1"),

  ADULT("2");

  public final String adult;

  Adult(String isAdult) {
    this.adult = isAdult;
  }

}
