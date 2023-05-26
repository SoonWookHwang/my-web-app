package com.example.mywebapp.domain.content;

import lombok.Getter;

@Getter
public enum ContentsType {

  FREE("1"),  //무료 컨텐츠

  PAYED("2");  // 유료 컨텐츠

  public final String status;

  ContentsType(String status) {
    this.status = status;
  }

}
