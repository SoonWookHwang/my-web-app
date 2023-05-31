package com.example.mywebapp.dto.request.contents;


import com.example.mywebapp.domain.content.ContentsType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ContentsRequestDto {

  private String title;

  @Enumerated(EnumType.STRING)
  private ContentsType isFree;

  private String isAdult;  // 일반 1 , 성인 2

  private String author;


}
