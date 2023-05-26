package com.example.mywebapp.dto.response;


import com.example.mywebapp.domain.content.Contents;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ContentsResponseEntity<T> {

  private HttpStatus status;

  private T result;

  private Error error;

  public static <T> ContentsResponseEntity<T> success(T result) {
    return new ContentsResponseEntity<>(HttpStatus.OK, result, null);
  }

  public static <T> ContentsResponseEntity<T> fail(HttpStatus status, String message) {
    return new ContentsResponseEntity<>(status, null,
        new Error(status.value(), message));
  }

  public static ContentsResponseEntity<List<Contents>> success(Page<Contents> result) {
    List<Contents> top3Contents = result.getContent();
    return new ContentsResponseEntity<>(HttpStatus.OK, top3Contents, null);
  }

  @Getter
  @AllArgsConstructor
  static class Error {

    private int statusCode;
    private String message;
  }
}

